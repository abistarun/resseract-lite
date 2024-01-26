import { Component, OnInit } from '@angular/core';
import { AbstractWidget } from '../abstract-widget';
import { WidgetSpecification } from '../../specs/widget-specification';
import { Configration } from '../../specs/analysis-configration';
import { AnalysisSpecification } from '../../specs/analysis-specification';
import { DataKey } from '../../specs/data-key';
import { HttpErrorResponse } from '@angular/common/http';
import { MatDialog } from '@angular/material/dialog';
import { CoreFetcherService } from '../../services/core-fetcher/core-fetcher.service';
import { CoreEventEmitterService } from '../../services/core-event-emiter/core-event-emitter.service';
import { SliceService } from '../../services/slice-service/slice-service.service';
import { AnalysisService } from '../../services/analysis-service/analysis-service.service';
import { HumanizePipe } from '../../pipes/humanize';

@Component({
  selector: 'app-data-table-widget',
  templateUrl: './data-table-widget.component.html',
  styleUrls: ['./data-table-widget.component.css']
})
export class DataTableWidgetComponent extends AbstractWidget {

  headers: Object[];
  data: any[];
  humanize = new HumanizePipe();

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
    this.specAdded = new AnalysisSpecification("GROUP_BY", new DataKey(widgetSpecification.configrations["dataKey"]), specConfig);
  }

  deserialize(data: any) {
    this.specAdded = data.specAdded;
    this.customOptions = data.customOptions;
  }

  serialize() {
    return {
      specAdded: this.specAdded,
      customOptions: this.customOptions,
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
        this.customOptions['title'].value = this.specAdded.configurations.properties["GROUPBY_COLUMN_NAME"] + "-wise Grouped Data";
      }

      this.headers = [];
      let indexName = this.specAdded.configurations.properties["GROUPBY_COLUMN_NAME"]
      this.headers.push(indexName);
      const columnNames = Object.keys(result[0].columns);
      this.headers = this.headers.concat(columnNames)

      this.data = [];
      result[0].index.forEach((value, index) => {
        let row = [];
        row.push(this.humanize.transform(value));
        columnNames.forEach(colName => {
          row.push(this.humanize.transform(result[0].columns[colName].data[index]));
        })
        this.data.push(row);
      })
      this.loadedStatus = true;
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

  reflow() { }
  applyOptions() { }
  getCustomOptions(): {} { return {} }
}
