import { Component, ViewChild, ElementRef } from '@angular/core';
import { mapChart, Options, Chart } from 'highcharts/highmaps';
import { AbstractWidget } from '../abstract-widget';
import { WidgetSpecification } from '../../specs/widget-specification';
import { Configration } from '../../specs/analysis-configration';
import { AnalysisSpecification } from '../../specs/analysis-specification';
import { DataKey } from '../../specs/data-key';
import { HttpErrorResponse } from '@angular/common/http';
import { AnalysisResult } from '../../specs/analysis-result';
import * as mappings from '../../specs/map-mappings';
import { MatDialog } from '@angular/material/dialog';
import { CoreFetcherService } from '../../services/core-fetcher/core-fetcher.service';
import { CoreEventEmitterService } from '../../services/core-event-emiter/core-event-emitter.service';
import { SliceService } from '../../services/slice-service/slice-service.service';
import { AnalysisService } from '../../services/analysis-service/analysis-service.service';
import { SliceOptionsDialogComponent } from '../../slice-options-dialog/slice-options-dialog.component';

@Component({
  selector: 'app-geo-map-widget',
  templateUrl: './geo-map-widget.component.html',
  styleUrls: ['./geo-map-widget.component.css']
})
export class GeoMapWidgetComponent extends AbstractWidget {

  mapType: string;

  @ViewChild('chartTarget', { static: false }) private chartTarget: ElementRef;
  chart: Chart;
  targets: string[] = [];

  getCustomOptions(): {} { return {}; }

  constructor(protected dialog: MatDialog,
    protected fetcherService: CoreFetcherService,
    protected emiiterService: CoreEventEmitterService,
    protected sliceService: SliceService,
    protected analysisService: AnalysisService) {
    super(dialog, fetcherService, emiiterService, sliceService, analysisService);
  }

  initialize(widgetSpecification: WidgetSpecification): void {
    let specConfig = new Configration();
    if (widgetSpecification.configrations["expression"]) {
      specConfig.properties["EXPRESSION"] = widgetSpecification.configrations["expression"];
    } else {
      specConfig.properties["TARGET_COLUMNS"] = widgetSpecification.configrations["selectedColumn"];
    }
    specConfig.properties["GROUPBY_COLUMN_NAME"] = widgetSpecification.configrations["xAxis"];
    this.mapType = widgetSpecification.configrations["mapType"];
    this.specAdded = new AnalysisSpecification("GROUP_BY", new DataKey(widgetSpecification.configrations["dataKey"]), specConfig);
  }

  deserialize(data: any) {
    this.specAdded = data.specAdded;
    this.customOptions = data.customOptions;
    this.mapType = data.mapType;
    this.targets = data.targets;
  }

  serialize() {
    return {
      specAdded: this.specAdded,
      customOptions: this.customOptions,
      mapType: this.mapType,
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

  private plotChart(result: AnalysisResult[]) {
    if (this.chart) {
      this.chart.destroy();
      this.chart = null;
    }
    let chartOptions: Options = {
      chart: {
        map: mappings.MAP_MAPPING[this.mapType].key
      },
      mapNavigation: {
        enabled: true,
        buttonOptions: {
          verticalAlign: 'bottom'
        }
      },
      tooltip: {
        formatter: function () {
          return this.point["name"] + ': ' + this.point.value;
        }
      },
      colorAxis: {
        showInLegend: false
      },
      series: this.prepareSeries(result[0])
    };
    this.chart = mapChart(this.chartTarget.nativeElement, chartOptions);
    this.applyOptions();
    this.loadedStatus = true;
  }

  private prepareSeries(result: AnalysisResult) {
    let series: any = [{
      name: "Map", showInLegend: false, dataLabels: {
        enabled: true
      }
    }];
    Object.keys(result.columns).forEach(colName => {
      if (!this.customOptions["series" + series.length + "Label"])
        this.customOptions["series" + series.length + "Label"] = {
          name: colName + " Label",
          value: colName,
          group: colName + " Series",
          chartPath: "name",
          chartElementId: "series" + series.length
        }
      if (!this.customOptions["series" + series.length + "Config"])
        this.customOptions["series" + series.length + "Config"] = {
          name: "Series " + colName + " Config : E.g map([<Gradient Colors>], <min>, <max>)",
          value: "map([#00ff00, #ff0000])",
          group: colName + " Series"
        }
      if (!this.customOptions["series" + series.length + "ShowDataLabels"])
        this.customOptions["series" + series.length + "ShowDataLabels"] = {
          name: "Show Data Labels",
          value: "false",
          group: colName + " Series",
          chartPath: "dataLabels.enabled",
          chartElementId: "series" + series.length,
        }
      this.customOptions["series" + series.length + "ShowDataLabels"]["transform"] = (value: string) => value == 'true';
      const data = result.columns[colName].data.map((ele, i) => [result.index[i], ele, ele]);
      series.push({
        id: "series" + series.length,
        data: data,
        keys: ['name', 'value', 'z'],
        joinBy: 'name',
        dataLabels: {
          format: '{point.name}'
        }
      })
    });
    return series;
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
      if (selectedTargets) { 
        this.targets = selectedTargets;
        this.applySliceOnTargets()
      }
    })
  }

  private applySliceOnTargets() {
    if (this.targets == null)
      return;
      
    if (this.targets && this.targets.length == 0) {
      this.sliceService.removePublisher(this.getWidgetId());
      return;
    }

    this.sliceService.registerPublisher(this.getWidgetId(), this.targets);
    this.chart.update({
      colorAxis: {
        showInLegend: false
      },
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
    });
    setTimeout(() => this.chart.redraw(), 1000);
  }

  private updateSeriesFromCustomOptions(series, id) {
    if (id == 0)
      return

    let config: string = this.customOptions["series" + id + "Config"].value;
    if (config.startsWith("map"))
      this.handleMapType(config, series);
  }

  private handleMapType(config: string, series: any) {
    let args = config.replace("map", "").replace("(", "").replace(")", "").trim()
    let colors = args.substr(1, args.indexOf("]") - 1).trim().split(",").map(e => e.trim())
    let minMax = args.substr(args.indexOf("]") + 1).split(",").map(e => e.trim())
    let min: number, max: number;
    if (minMax[1])
      min = +minMax[1]
    if (minMax[2])
      max = +minMax[2]

    series.update({
      type: "map"
    });
    let stopBands = colors.map((e, i) => [i / (colors.length - 1), e]);
    this.chart.update({
      colorAxis: {
        showInLegend: true,
        stops: <any>stopBands,
        min: min,
        max: max
      }
    })
  }

  reflow() {
    if (this.chart)
      setTimeout(() => {
        this.chart.reflow();
      }, 500);
  }

  applyOptions() {
    this.applyOptionsToChartObject(this.chart);
    this.chart.series.forEach((ser, i) => {
      this.updateSeriesFromCustomOptions(ser, i);
    });
    setTimeout(() => this.chart.redraw(), 2000);
  }
}
