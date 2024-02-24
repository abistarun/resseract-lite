import { Component, ViewChild, ElementRef } from '@angular/core';
import { WidgetSpecification } from '../../specs/widget-specification';
import { AnalysisSpecification } from '../../specs/analysis-specification';
import { chart } from 'highcharts';
import { AnalysisResult } from '../../specs/analysis-result';
import { HttpErrorResponse } from '@angular/common/http';
import { Configration } from '../../specs/analysis-configration';
import { DataKey } from '../../specs/data-key';
import { DatePipe } from '@angular/common';
import { AbstractWidget } from '../abstract-widget';
import { MatDialog } from '@angular/material/dialog';
import { CoreFetcherService } from '../../services/core-fetcher/core-fetcher.service';
import { CoreEventEmitterService } from '../../services/core-event-emiter/core-event-emitter.service';
import { SliceService } from '../../services/slice-service/slice-service.service';
import { AnalysisService } from '../../services/analysis-service/analysis-service.service';
import { SliceOptionsDialogComponent } from '../../slice-options-dialog/slice-options-dialog.component';

@Component({
  selector: 'app-basic-chart-widget',
  templateUrl: './basic-chart-widget.component.html',
  styleUrls: ['./basic-chart-widget.component.css']
})
export class BasicChartWidgetComponent extends AbstractWidget {

  @ViewChild('chartTarget', { static: false }) private chartTarget: ElementRef;

  private chartType: string;
  chart: Highcharts.Chart;
  targets: string[] = [];

  private chartMapping = {
    bar: "column",
    line: "line",
    pie: "pie",
    area: "area"
  }

  getCustomOptions(): {} {
    return {
      xAxisLabel: {
        name: "X Axis Label",
        value: "",
        group: "X Axis",
        chartPath: "xAxis.title.text"
      },
      yAxisLabel: {
        name: "Y Axis Label",
        value: "",
        group: "Y Axis",
        chartPath: "yAxis.title.text"
      },
      dateFormat: {
        name: "Date Format",
        value: "dd/MMM/yyyy",
        group: "X Axis",
        chartPath: "data.dateFormat"
      }
    };
  }

  constructor(protected dialog: MatDialog,
    protected fetcherService: CoreFetcherService,
    protected emiiterService: CoreEventEmitterService,
    protected sliceService: SliceService,
    protected analysisService: AnalysisService) {
    super(dialog, fetcherService, emiiterService, sliceService, analysisService);
  }

  initialize(widgetSpecification: WidgetSpecification) {
    let specConfig = new Configration();
    if (widgetSpecification.configrations["expression"]) {
      specConfig.properties["EXPRESSION"] = widgetSpecification.configrations["expression"];
    } else {
      specConfig.properties["TARGET_COLUMNS"] = widgetSpecification.configrations["selectedColumn"];
    }
    specConfig.properties["GROUPBY_COLUMN_NAME"] = widgetSpecification.configrations["xAxis"];
    this.chartType = widgetSpecification.configrations["chartType"];
    this.specAdded = new AnalysisSpecification("GROUP_BY", new DataKey(widgetSpecification.configrations["dataKey"]), specConfig);
  }

  deserialize(data: any) {
    this.specAdded = data.specAdded;
    this.customOptions = data.customOptions;
    this.chartType = data.chartType;
    this.targets = data.targets;
  }

  serialize() {
    return {
      specAdded: this.specAdded,
      customOptions: this.customOptions,
      chartType: this.chartType,
      targets: this.targets
    };
  }

  refetch(hard: boolean, showLoading: boolean) {
    if (showLoading)
      this.loadedStatus = false;
    this.errorMessage = null;
    let spec = this.processSlice(this.specAdded);
    this.fetcherService.runAnalysis([spec]).subscribe((result) => {
      if (hard || !this.customOptions['title'].value) {
        let targetColumns = this.specAdded.configurations.properties["TARGET_COLUMNS"];
        if (!targetColumns)
          targetColumns = "Expression Result";
        this.customOptions['title'].value = targetColumns + " by " + this.specAdded.configurations.properties["GROUPBY_COLUMN_NAME"];
      }
      if (hard || !this.customOptions['xAxisLabel'].value)
        this.customOptions['xAxisLabel'].value = spec.configurations.properties["GROUPBY_COLUMN_NAME"]
      if (hard || !this.customOptions['yAxisLabel'].value)
        this.customOptions['yAxisLabel'].value = Object.keys(result[0].columns)[0]
      this.plotChart(result);
      setTimeout(() => this.applySliceOnTargets(), 2000);
    },
      err => {
        if (err instanceof HttpErrorResponse && err.status == 499) {
          this.errorMessage = err.error.message;
        } else {
          this.errorMessage = err.message;
        }
        this.loadedStatus = true;
      });
  }

  plotChart(result: AnalysisResult[]) {
    this.processDates(result);
    let formatDatesFunction = (value: any) => this.formatDatesFunction(value)
    let chartOptions: Highcharts.Options = {
      chart: {
        type: this.chartMapping[this.chartType],
        zooming: {
          type: 'x'
        }
      },
      plotOptions: {
        line: {
          lineWidth: 3,
          marker: {
            enabled: false
          }
        },
        spline: {
          lineWidth: 3,
          marker: {
            enabled: false
          }
        },
        column: {
          dataLabels: {
            enabled: true,
            format: '{point.y:,.2f}'
          }
        },
        pie: {
          dataLabels: {
            formatter: function () {
              var sliceIndex = this.point.index;
              var sliceName = this.series.chart.axes[0].categories[sliceIndex];
              return sliceName;
            }
          }
        }
      },
      xAxis: {
        categories: result[0].index,
        labels: {
          formatter: function () {
            return formatDatesFunction(this.value);
          }
        }
      },
      series: this.prepareSeries(result[0])
    };
    this.chart = chart(this.chartTarget.nativeElement, chartOptions);
    this.applyOptions();
    this.loadedStatus = true;
  }

  private formatDatesFunction(value: string) {
    if (this.customOptions['dateFormat'] && this.customOptions['dateFormat'].value && this.isXAxisDate()) {
      var datePipe = new DatePipe("en-US");
      return datePipe.transform(value, this.customOptions['dateFormat'].value);
    }
    else
      return value;
  }

  private processDates(result: AnalysisResult[]) {
    if (this.isXAxisDate())
      result[0].index = result[0].index.map(lng => new Date(lng as number));
  }

  private isXAxisDate() {
    return this.analysisService.getDataInfo(this.specAdded.dataKey).columnProperties[this.specAdded.configurations.properties["GROUPBY_COLUMN_NAME"]]["dataType"] == "DATE";
  }

  private prepareSeries(result: AnalysisResult) {
    let series = [];
    let oldSeriesOptions = Object.keys(this.customOptions).filter(x => x.startsWith("series"))
    let newSeriesOptions = [];

    Object.keys(result.columns).forEach(colName => {
      const currLabelOption = "series" + colName + "Label";
      const currColorOption = "series" + colName + "Color";
      const currTypeOption = "series" + colName + "Type";
      const currSymbolOption = "series" + colName + "Symbol";
      const currShowLabelsOption = "series" + colName + "ShowDataLabels";
      
      newSeriesOptions.push(currLabelOption);
      newSeriesOptions.push(currColorOption);
      newSeriesOptions.push(currTypeOption);
      newSeriesOptions.push(currSymbolOption);
      newSeriesOptions.push(currShowLabelsOption);

      if (!this.customOptions[currLabelOption])
        this.customOptions[currLabelOption] = {
          name: colName + " Series Label",
          value: colName,
          group: colName + " Series",
          type: 'text',
          chartPath: "name",
          chartElementId: "series" + colName
        }

      if (!this.customOptions[currColorOption])
        this.customOptions[currColorOption] = {
          name: colName + " Series Color",
          value: null,
          group: colName + " Series",
          type: 'text',
          chartPath: "color",
          chartElementId: "series" + colName
        }
      if (!this.customOptions[currTypeOption])
        this.customOptions[currTypeOption] = {
          name: colName + " Series Type",
          value: null,
          possibleValues: ['spline', 'line', 'scatter'],
          group: colName + " Series",
          type: 'list',
          chartPath: "type",
          chartElementId: "series" + colName
        }
      if (!this.customOptions[currSymbolOption])
        this.customOptions[currSymbolOption] = {
          name: colName + " Series Symbol",
          value: null,
          group: colName + " Series",
          type: 'text',
          chartPath: "marker.symbol",
          forSeriesType: ['scatter', 'line'],
          chartElementId: "series" + colName
        }
      if (!this.customOptions[currShowLabelsOption])
        this.customOptions[currShowLabelsOption] = {
          name: "Show Data Labels",
          value: "false",
          possibleValues: ['false', 'true'],
          group: colName + " Series",
          type: 'list',
          chartPath: "dataLabels.enabled",
          chartElementId: "series" + colName,
        }
      this.customOptions[currShowLabelsOption]["transform"] = (value: string) => value == 'true'
      
      let data = result.columns[colName].data
      data.forEach(function (element, index) {
        if (element === 0) {
          data[index] = null;
        }
      });
      series.push({
        id: "series" + colName,
        data: data
      })
    });
    oldSeriesOptions.forEach(oldSeries => {
      if (!newSeriesOptions.includes(oldSeries)) {
        delete this.customOptions[oldSeries];
      }
    });
    return series;
  }

  reflow() {
    if (this.chart)
      setTimeout(() => {
        this.chart.reflow();
      }, 500);
  }

  handleSlice() {
    let dialogRef = this.dialog.open(SliceOptionsDialogComponent, {
      width: '1000px',
      autoFocus: false,
      data: {
        widgetId: this.getWidgetId()
      }
    });

    dialogRef.afterClosed().subscribe(selectedTargets => {
      this.targets = selectedTargets;
      this.applySliceOnTargets();
    });
  }

  applySliceOnTargets() {
    if (this.targets == null)
      return;

    if (this.targets.length == 0) {
      this.sliceService.removePublisher(this.getWidgetId());
      this.chart.update({
        plotOptions: {
          series: {
            allowPointSelect: false
          }
        }
      })
      return;
    }

    this.sliceService.registerPublisher(this.getWidgetId(), this.targets);
    this.chart.update({
      plotOptions: {
        series: {
          allowPointSelect: true,
          cursor: 'pointer',
          point: {
            events: {
              click: (event) => {
                let sliceValue = event.point.category;
                if (event.point.series["slicedValue"] == sliceValue) {
                  sliceValue = null;
                }
                const sliceConfig = [{
                  dataKey: this.specAdded.dataKey.key,
                  dataType: 'CATEGORICAL',
                  column: this.specAdded.configurations.properties["GROUPBY_COLUMN_NAME"],
                  data: {
                    uniqueValues: sliceValue == null ? [] : [{ name: sliceValue, selected: true }]
                  }
                }];
                const expr = this.sliceService.buildExpression(sliceConfig, null);
                this.sliceService.publish(this.getWidgetId(), expr);
                this.sliceService.apply(this.getWidgetId());
                event.point.series["slicedValue"] = sliceValue;
              }
            }
          }
        }
      }
    })
  }

  applyOptions() {
    this.applyOptionsToChartObject(this.chart);
    let formatDatesFunction = (value): string => this.formatDatesFunction(value)
    this.chart.xAxis[0].update({
      labels: {
        formatter: function () {
          return formatDatesFunction(this.value);
        }
      }
    })
  }
}
