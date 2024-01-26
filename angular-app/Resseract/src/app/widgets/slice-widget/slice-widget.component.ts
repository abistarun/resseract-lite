import { Component, OnInit, Input, ViewChild } from '@angular/core';
import { WidgetSpecification } from '../../specs/widget-specification';
import { SlicePanelComponent } from '../../slice-panel/slice-panel.component';
import { SliceService } from '../../services/slice-service/slice-service.service';
import { CoreEventEmitterService } from '../../services/core-event-emiter/core-event-emitter.service';
import { MatDialog } from '@angular/material/dialog';
import { ChartOptionsDialogComponent } from '../../chart-options-dialog/chart-options-dialog.component';
import { SliceOptionsDialogComponent } from '../../slice-options-dialog/slice-options-dialog.component';
import { AbstractWidget } from '../abstract-widget';
import { CoreFetcherService } from '../../services/core-fetcher/core-fetcher.service';
import { AnalysisService } from '../../services/analysis-service/analysis-service.service';

@Component({
  selector: 'app-slice-widget',
  templateUrl: './slice-widget.component.html',
  styleUrls: ['./slice-widget.component.css']
})
export class SliceWidgetComponent extends AbstractWidget {

  @ViewChild('slicePanel', { static: true }) slicePanel: SlicePanelComponent;
  autoApply: boolean = true;
  targets: any[] = null;

  constructor(protected dialog: MatDialog,
    protected fetcherService: CoreFetcherService,
    protected emiiterService: CoreEventEmitterService,
    protected sliceService: SliceService,
    protected analysisService: AnalysisService) {
    super(dialog, fetcherService, emiiterService, sliceService, analysisService);
  }

  initialize() {
    this.customOptions['title'].value = "Slice";
    this.slicePanel.addSelector();
  }

  initializeWidgetCallbacks(widgetSpecification: WidgetSpecification) {
    this.refetch();
    this.sliceService.registerPublisher(widgetSpecification.widgetId, null);
    this.sliceService.registerSubscriber(widgetSpecification.widgetId, () => this.refetch(), this.customOptions);
    this.slicePanel.setBaseWidgetId(widgetSpecification.widgetId);
    if (this.targets)
      setTimeout(() => {
        this.sliceService.updateTargets(widgetSpecification.widgetId, this.targets);
      }, 300);
    if (this.autoApply)
      this.slicePanel.setOnExpressionChangeCallback(() => this.applySlice());
    else
      this.slicePanel.setOnExpressionChangeCallback(undefined);
    if (Object.keys(this.slicePanel.buildExpression()).length > 0)
      this.applySlice(false);
  }

  refetch() {
    setTimeout(() => this.slicePanel.refetch(), 0);
  }

  deserialize(data: any) {
    this.customOptions = data.customOptions;
    this.autoApply = data.autoApply;
    this.slicePanel.selectors = data.selectors;
    this.slicePanel.allowMultiCategory = data.allowMultiCategory;
    this.slicePanel.allowNumericalRange = data.allowNumericalRange != undefined ? data.allowNumericalRange : true;
    this.slicePanel.showColumnSelectors = data.showColumnSelectors;
    this.targets = data.targets;
  }

  serialize() {
    return {
      customOptions: this.customOptions,
      autoApply: this.autoApply,
      selectors: this.slicePanel.selectors,
      allowMultiCategory: this.slicePanel.allowMultiCategory,
      allowNumericalRange: this.slicePanel.allowNumericalRange,
      showColumnSelectors: this.slicePanel.showColumnSelectors,
      targets: this.sliceService.getTargets(this.getWidgetId())
    };
  }

  selectTargets() {
    let dialogRef = this.dialog.open(SliceOptionsDialogComponent, {
      width: '1000px',
      autoFocus: false,
      data: {
        widgetId: this.getWidgetId()
      }
    });

    dialogRef.afterClosed().subscribe(selectedTargets => {
      if (selectedTargets.length == 0) {
        this.errorMessage = "Please select atleast one target";
        return;
      }
      this.sliceService.updateTargets(this.getWidgetId(), selectedTargets)
      this.sliceService.refreshAll();
    })
  }

  applySlice(apply: boolean = true) {
    let expression = this.slicePanel.buildExpression();
    this.sliceService.publish(this.getWidgetId(), expression);
    if (apply)
      this.sliceService.apply(this.getWidgetId());
  }

  autoApplyClick() {
    if (!this.autoApply)
      this.slicePanel.setOnExpressionChangeCallback(() => this.applySlice());
    else
      this.slicePanel.setOnExpressionChangeCallback(undefined);
  }

  reflow() { }
  applyOptions() { }
  getCustomOptions(): {} { return {} }
}
