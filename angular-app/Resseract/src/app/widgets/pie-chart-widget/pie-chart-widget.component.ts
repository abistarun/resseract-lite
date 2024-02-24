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
  selector: 'app-pie-chart-widget',
  templateUrl: './pie-chart-widget.component.html',
  styleUrls: ['./pie-chart-widget.component.css']
})
export class PieChartWidgetComponent extends AbstractWidget {
  
  @ViewChild('chartTarget', { static: false }) private chartTarget: ElementRef;

  private chartType: string;
  chart: Highcharts.Chart;
  targets: string[] = [];

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
    let chartOptions: Highcharts.Options = {
      chart: {
        type: 'pie'
      },
      plotOptions: {
        pie: {
          dataLabels: {
            enabled: true,
            format: '<b>{point.name}</b>:<br>{point.percentage:.1f} %',
          }
        }
      },
      series: this.prepareSeries(result[0])
    };
    this.chart = chart(this.chartTarget.nativeElement, chartOptions);
    this.applyOptions();
    this.loadedStatus = true;
  }

  private prepareSeries(result: AnalysisResult) {
    let series = [];
    let columnName = Object.keys(result.columns)[0]
    let data = result.columns[columnName].data;
    let seriesData = []
    data.forEach(function (element, i) {
      if (element !=0) {
        seriesData.push({
          name: result.index[i],
          y: element
        });
      }
    });
    series.push({
      id: "pieChartSeries",
      name: columnName,
      data: seriesData
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
                let sliceValue = event.point.name;
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
  }

  getCustomOptions(): {} {
    return {};
  }
}
