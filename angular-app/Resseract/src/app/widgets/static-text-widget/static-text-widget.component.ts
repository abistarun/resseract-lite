import { Component, OnInit } from '@angular/core';
import { AbstractWidget } from '../abstract-widget';
import { WidgetSpecification } from '../../specs/widget-specification';
import { MatDialog } from '@angular/material/dialog';
import { CoreFetcherService } from '../../services/core-fetcher/core-fetcher.service';
import { CoreEventEmitterService } from '../../services/core-event-emiter/core-event-emitter.service';
import { SliceService } from '../../services/slice-service/slice-service.service';
import { AnalysisService } from '../../services/analysis-service/analysis-service.service';

@Component({
  selector: 'app-static-text-widget',
  templateUrl: './static-text-widget.component.html',
  styleUrls: ['./static-text-widget.component.css']
})
export class StaticTextWidgetComponent extends AbstractWidget {
  
  value: string;
  disabled: boolean = false;

  constructor(protected dialog: MatDialog,
    protected fetcherService: CoreFetcherService,
    protected emiiterService: CoreEventEmitterService,
    protected sliceService: SliceService,
    protected analysisService: AnalysisService) {
    super(dialog, fetcherService, emiiterService, sliceService, analysisService);
  }

  initialize(widgetSpecification: WidgetSpecification): void {
    this.customOptions["title"].value = "Title"
  }

  serialize() {
    return {
      value: this.value,
      customOptions: this.customOptions,
      disabled: this.disabled
    };
  }

  deserialize(data: any): void {
    this.customOptions = data.customOptions;
    this.disabled = data.disabled;
    this.value = data.value;
  }
  
  getCustomOptions(): {} {return {}}
  refetch(hard: boolean, showLoading: boolean): void {}
  reflow(): void {}
  applyOptions(): void {}
}
