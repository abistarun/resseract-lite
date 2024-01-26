import { Component, OnInit } from '@angular/core';
import { CoreFetcherService } from '../services/core-fetcher/core-fetcher.service';
import { AnalysisSpecification } from '../specs/analysis-specification';
import { DataKey } from '../specs/data-key';
import { Configration } from '../specs/analysis-configration';
import { MatDialog } from '@angular/material/dialog';
import { ConfigrationDialogComponent } from '../configration-dialog/configration-dialog.component';
import { CoreEventEmitterService } from '../services/core-event-emiter/core-event-emitter.service';
import { SliceService } from '../services/slice-service/slice-service.service';
import { AnalysisService } from '../services/analysis-service/analysis-service.service';
import { HttpErrorResponse } from '@angular/common/http';

@Component({
  selector: 'app-slice-panel',
  templateUrl: './slice-panel.component.html',
  styleUrls: ['./slice-panel.component.css']
})
export class SlicePanelComponent implements OnInit {
  Object = Object;

  loadedStatus: boolean = true;
  error: string;
  selectors: any[] = [];
  allowMultiCategory: boolean = true;
  showColumnSelectors: boolean = true;
  onColumnSelectExternalCallback: Function;
  onExpressionChangeCallback: Function;
  baseWidgetId: string;
  dataKeys: string[];
  allowNumericalRange: boolean = true;

  constructor(private dialog: MatDialog,
    private fetcherService: CoreFetcherService,
    private emitterService: CoreEventEmitterService,
    private sliceService: SliceService,
    private analysisService: AnalysisService) { }

  ngOnInit() {
    this.dataKeys = this.analysisService.getAllDataKeys();
  }

  setBaseWidgetId(widgetId) {
    this.baseWidgetId = widgetId;
  }

  onDataKeySelect = (selector) => (dataKey) => {
    selector.columns.splice(0, selector.columns.length)
    selector.selectedDataKey = null;
    selector.selectedColumn = null;
    selector.dataType = null;
    selector.data = null;
    if (dataKey) {
      selector.selectedDataKey = dataKey;
      Object.keys(this.analysisService.getDataInfo(new DataKey(dataKey)).columnProperties).forEach(columnName => {
        selector.columns.push(columnName);
      });
    }
  }

  addSelector() {
    this.selectors.push({
      selectedDataKey: null,
      columns: [],
      selectedColumn: null,
    });
  }

  onColumnSelect = (selector) => (column) => {
    if (column) {
      selector.selectedColumn = column;
      let specConfig = new Configration();
      specConfig.properties["GROUPBY_COLUMN_NAME"] = selector.selectedColumn;
      selector.spec = new AnalysisSpecification("GROUP_BY", new DataKey(selector.selectedDataKey), specConfig);
      this.fetchForSelector(selector);
    } else {
      selector.selectedColumn = null;
      selector.dataType = null;
      selector.data = null;
    }
    if (this.onColumnSelectExternalCallback)
      this.onColumnSelectExternalCallback(this.selectors.map(s => s.selectedColumn).filter(sc => sc));
  }

  refetch() {
    this.error = null;
    this.selectors.forEach(selector => {
      if (selector.selectedDataKey) {
        let selectedValues: [] = [];
        if (selector.dataType == "CATEGORICAL") {
          selectedValues = selector.data.uniqueValues.filter(value => value.selected).map(value => value.name);
        }
        this.fetchForSelector(selector,
          selector.data ? selector.data.selectedRange : null,
          selector.data ? selector.data.selectedValue : null,
          selectedValues);
      }
    });
  }

  configureAnalysis() {
    let specs = {};
    this.selectors.forEach((selector, i) => {
      if (selector.spec)
        specs[i] = selector.spec;
    });
    if (!specs) {
      this.emitterService.emitMessageEvent("No Valid Selectors");
      return
    }
    let dialogRef = this.dialog.open(ConfigrationDialogComponent, {
      width: '1000px',
      autoFocus: false,
      data: {
        analysisSpecifications: Object.values(specs),
      }
    });

    dialogRef.afterClosed().subscribe(specs => {
      if (specs) {
        specs.forEach((spec, i) => {
          let selectorIndex = Object.keys(specs)[i]
          this.selectors[selectorIndex].spec = spec;
        });
        this.refetch();
      }
    });
  }

  private processSlice(baseSpec: AnalysisSpecification) {
    if (!this.baseWidgetId)
      return baseSpec;
    let spec = JSON.parse(JSON.stringify(baseSpec));
    let sliceExpr = spec.configurations.properties['SLICE_EXPRESSION'];
    let globalSlice = this.sliceService.getExpressions(this.baseWidgetId)[spec.dataKey.key];
    if (globalSlice)
      if (sliceExpr) {
        spec.configurations.properties['SLICE_EXPRESSION'] = sliceExpr + "&& (" + globalSlice + ")";
      } else {
        spec.configurations.properties['SLICE_EXPRESSION'] = globalSlice;
      }
    return spec;
  }

  private fetchForSelector(selector: any, selectedRange: [] = null, selectedValue: any = null, selectedValues: any[] = []) {
    let spec = this.processSlice(selector.spec);
    let specs = [spec];
    const dataInfo = this.analysisService.getDataInfo(new DataKey(selector.selectedDataKey));
    if (!dataInfo) {
      this.error = "Data not present"
      return
    }
    let dataType = dataInfo.columnProperties[selector.selectedColumn].dataType;
    this.loadedStatus = false;
    this.fetcherService.runAnalysis(specs).subscribe(result => {      
      if (dataType == 'NUMERICAL') {
        const min = Math.min(...result[0].index.map(d => Number.parseFloat(<string>d)));
        const max = Math.max(...result[0].index.map(d => Number.parseFloat(<string>d)));
        selector.data = {
          min: min,
          max: max,
          selectedValue: this.allowNumericalRange ? null : (selectedValue == null ? min : selectedValue),
          selectedRange: this.allowNumericalRange ? (selectedRange == null ? [min, max] : selectedRange) : null
        };
      } else if (dataType == 'CATEGORICAL') {
        selector.data = {
          uniqueValues: result[0].index.map(e => {
            if (e == null) {
              e = "null"
            }
            return { name: e, selected: selectedValues.includes(e) ? true : false }
          })
        };
      }
      selector.dataType = dataType;
      this.loadedStatus = true;
    }, err => {
      if (err instanceof HttpErrorResponse && err.status == 499) {
        this.error = err.error.message;
      } else {
        this.error = err.message;
      }
      this.loadedStatus = true;
    });
  }

  selectCard(selector, value) {
    if (!this.allowMultiCategory)
      selector.data.uniqueValues.forEach(element => {
        if (element.name != value.name)
          element.selected = false;
      });
    value.selected = !value.selected;
    if (this.onExpressionChangeCallback)
      this.onExpressionChangeCallback();
  }

  onRangeChange() {
    if (this.onExpressionChangeCallback)
      this.onExpressionChangeCallback();
  }

  toggleMultiCategory($event) {
    $event.stopPropagation();
    if (this.allowMultiCategory)
      this.selectors.forEach(selector => {
        if (selector.dataType == 'CATEGORICAL' && selector.data.uniqueValues) {
          selector.data.uniqueValues.forEach(element => {
            element.selected = false;
          });
        }
      });
  }

  toggleAllowNumericalRange($event) {
    $event.stopPropagation();
    if (this.allowNumericalRange) {
      this.selectors.forEach(selector => {
        if (selector.dataType == 'NUMERICAL' && !selector.data.selectedValue) {
          selector.data.selectedValue = selector.data.selectedRange[0];
          selector.data.selectedRange = null;
        }
      });
    }
    else if (!this.allowNumericalRange) {
      this.selectors.forEach(selector => {
        if (selector.dataType == 'NUMERICAL' && !selector.data.selectedRange) {
          selector.data.selectedRange = [selector.data.selectedValue, selector.data.selectedValue];
          selector.data.selectedValue = null;
        }
      });
    }
  }

  buildExpression() {
    let sliceExprConfig = [];
    this.selectors.forEach(selector => {
      sliceExprConfig.push({
        dataKey: selector.selectedDataKey,
        dataType: selector.dataType,
        column: selector.selectedColumn,
        data: selector.data
      })
    });
    return this.sliceService.buildExpression(sliceExprConfig, this.allowNumericalRange);
  }

  setOnColumnSelectCallback(callback) {
    this.onColumnSelectExternalCallback = callback;
  }

  setOnExpressionChangeCallback(callback) {
    this.onExpressionChangeCallback = callback;
  }
}
