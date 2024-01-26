import { Component, OnInit, Input } from '@angular/core';
import { AnalysisSpecification } from '../../specs/analysis-specification';
import { WidgetSpecification } from '../../specs/widget-specification';
import { MatDialog } from '@angular/material/dialog';
import { SliceService } from '../../services/slice-service/slice-service.service';
import { CoreFetcherService } from '../../services/core-fetcher/core-fetcher.service';
import { CoreEventEmitterService } from '../../services/core-event-emiter/core-event-emitter.service';
import { Configration } from '../../specs/analysis-configration';
import { DataKey } from '../../specs/data-key';
import { HttpErrorResponse } from '@angular/common/http';
import { ConfigrationDialogComponent } from '../../configration-dialog/configration-dialog.component';
import { AbstractWidget } from '../abstract-widget';
import { AnalysisService } from '../../services/analysis-service/analysis-service.service';

@Component({
  selector: 'app-image-widget',
  templateUrl: './image-widget.component.html',
  styleUrls: ['./image-widget.component.css']
})
export class ImageWidgetComponent extends AbstractWidget {

  imageLink: any = null;

  constructor(protected dialog: MatDialog,
    protected fetcherService: CoreFetcherService,
    protected emiiterService: CoreEventEmitterService,
    protected sliceService: SliceService,
    protected analysisService: AnalysisService) {
    super(dialog, fetcherService, emiiterService, sliceService, analysisService);
  }
  
  initialize(widgetSpecification: WidgetSpecification) {
    let specConfig = new Configration();
    let columnName = widgetSpecification.configrations["imageColumn"];
    specConfig.properties["GROUPBY_COLUMN_NAME"] = columnName;
    this.specAdded = new AnalysisSpecification("GROUP_BY", new DataKey(widgetSpecification.configrations["dataKey"]), specConfig);
  }

  deserialize(data: any) {
    this.specAdded = data.specAdded;
    this.imageLink = data.imageLink;
  }

  serialize() {
    return {
      specAdded: this.specAdded,
      imageLink: this.imageLink,
    };
  }

  refetch(hard: boolean, showLoading: boolean) {
    if (showLoading)
      this.loadedStatus = false;
    this.imageLink = null;
    this.errorMessage = null;
    let spec = this.processSlice(this.specAdded);
    this.fetcherService.runAnalysis([spec]).subscribe((result) => {
      const imageLinks = result[0].index;
      if (imageLinks.length == 1)
        this.imageLink = imageLinks[0];
      else
        this.errorMessage = "Multiple images found. Please slice data."
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

  reflow() {}
  applyOptions() {}
  getCustomOptions(): {} {return {}}
}
