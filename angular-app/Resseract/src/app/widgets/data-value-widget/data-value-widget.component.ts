import { Component, ViewChild, ElementRef } from '@angular/core';
import { WidgetSpecification } from '../../specs/widget-specification';
import { Configration } from '../../specs/analysis-configration';
import { AnalysisSpecification } from '../../specs/analysis-specification';
import { DataKey } from '../../specs/data-key';
import { HttpErrorResponse } from '@angular/common/http';
import { AbstractWidget } from '../abstract-widget';
import { MatDialog } from '@angular/material/dialog';
import { CoreFetcherService } from '../../services/core-fetcher/core-fetcher.service';
import { CoreEventEmitterService } from '../../services/core-event-emiter/core-event-emitter.service';
import { SliceService } from '../../services/slice-service/slice-service.service';
import { AnalysisService } from '../../services/analysis-service/analysis-service.service';
import { HumanizePipe } from 'src/app/pipes/humanize';

@Component({
  selector: 'app-data-value-widget',
  templateUrl: './data-value-widget.component.html',
  styleUrls: ['./data-value-widget.component.css']
})
export class DataValueWidgetComponent extends AbstractWidget {

  @ViewChild('dataValueElement', { static: false }) dataValueElement: ElementRef;
  @ViewChild('unitsElement', { static: false }) unitsElement: ElementRef;

  private columnName: string;
  private value: string;
  humanize = new HumanizePipe();

  constructor(protected dialog: MatDialog,
    protected fetcherService: CoreFetcherService,
    protected emiiterService: CoreEventEmitterService,
    protected sliceService: SliceService,
    protected analysisService: AnalysisService) {
    super(dialog, fetcherService, emiiterService, sliceService, analysisService);
  }
  
  getCustomOptions() {
    return {
      units: {
        name: "Units",
        value: "",
        group: "Formatting"
      },
      fontSize: {
        name: "Font Size",
        value: "",
        group: "Formatting"
      },
      threshold: {
        name: "Threshold",
        value: 0,
        group: "Formatting"
      }
    }
  }

  initialize(widgetSpecification: WidgetSpecification) {
    let specConfig = new Configration();
    this.columnName = widgetSpecification.configrations["selectedColumn"];
    if (widgetSpecification.configrations["expression"]) {
      specConfig.properties["EXPRESSION"] = widgetSpecification.configrations["expression"];
      this.columnName = "Expression Result";
    } else {
      specConfig.properties["TARGET_COLUMNS"] = this.columnName;
    }
    this.specAdded = new AnalysisSpecification("GROUP_BY", new DataKey(widgetSpecification.configrations["dataKey"]), specConfig);
  }

  deserialize(data: any) {
    this.specAdded = data.specAdded;
    this.customOptions = data.customOptions;
    this.columnName = data.columnName;
    this.value = data.value;
  }

  serialize() {
    return {
      specAdded: this.specAdded,
      customOptions: this.customOptions,
      columnName: this.columnName,
      value: this.value
    };
  }

  refetch(hard: boolean = false, showLoading: boolean = true) {
    if (showLoading)
      this.loadedStatus = false;
    this.errorMessage = null;
    let spec = this.processSlice(this.specAdded);
    this.fetcherService.runAnalysis([spec]).subscribe((result) => {
      let columnName = Object.keys(result[0].columns)[0];
      this.value = this.humanize.transform(result[0].columns[columnName].data[0]);
      if (!this.customOptions['title'].value)
        this.customOptions['title'].value = this.columnName;
      this.reflow();
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

  reflow() {
    setTimeout(() => {
      this.setFont();
    }, 500);
  }

  private setFont() {
    let ele = this.dataValueElement;
    if (ele) {
      let fontSize: number;
      if (this.customOptions['fontSize'] && this.customOptions['fontSize'].value) {
        fontSize = this.customOptions['fontSize'].value;
      } else {
        let width = ele.nativeElement.offsetWidth;
        let height = ele.nativeElement.offsetHeight;
        fontSize = (width * 0.25) < (height * 0.4) ? width * 0.25 : height * 0.4;
      }
      let threshold: number;
      if (this.customOptions['threshold'] && this.customOptions['threshold'].value) {
        threshold = this.customOptions['threshold'].value;
      } else {
        threshold = 0;
      }
      let floatValue: number;
      if (this.value) {
        floatValue = parseFloat(this.value.replace(/,/g, ''));
      }
      ele.nativeElement.style.setProperty('font-size', fontSize + "px");
      if (floatValue && !isNaN(floatValue))
        ele.nativeElement.style.setProperty('color', floatValue >= threshold ? 'green' : 'red');
      let unitSize = (fontSize * 0.25);
      if (this.unitsElement && this.unitsElement.nativeElement)
        this.unitsElement.nativeElement.style.setProperty('font-size', unitSize + "px");
      if (!this.customOptions['fontSize']) {
        this.customOptions['fontSize'] = {
          name: "Font Size",
          value: ""
        }
      }
      this.customOptions['fontSize'].value = fontSize;
    }
  }

  applyOptions() {
    this.reflow();
  }
}
