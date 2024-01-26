import { Component, OnInit, Inject } from '@angular/core';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { AnalysisService } from '../../services/analysis-service/analysis-service.service';
import { DataKey } from '../../specs/data-key';
import * as mappings from '../../specs/map-mappings';

@Component({
  selector: 'app-generic-widget-dialog',
  templateUrl: './generic-widget-dialog.component.html',
  styleUrls: ['./generic-widget-dialog.component.css']
})
export class GenericWidgetDialogComponent implements OnInit {

  allMapTypes: string[] = Object.keys(mappings.MAP_MAPPING);

  config: any = {
    dataKey: false,
    xAxis: false,
    multipleYAxis: false,
    columnOrExpression: false,
    allowCategorialInColumn: false,
    range: false,
    imageColumn: false,
    mapType: false,
  };

  loadedStatus: boolean = true;
  categoricalColumns: string[] = [];
  dateColumns: string[] = [];
  numericalColumns: string[] = [];
  errorMessage: string = "";

  selectedMapType: string = null;
  selectedDataKey: string = null;
  selectedXAxis: string = null;
  selectedColumn: string = null;
  selectedImageColumn: string = null;
  expression: string = null;
  selectedYAxis: string[] = [];
  columnOrExpressionColumns: string[] = [];
  minRange: number = 0;
  maxRange: number = 100;
  datakeys: string[] = [];

  constructor(@Inject(MAT_DIALOG_DATA) public data: any,
    public dialogRef: MatDialogRef<GenericWidgetDialogComponent>,
    private analysisService: AnalysisService
  ) {
    if (data.config)
      this.config = data.config;
  }

  ngOnInit() {
    this.datakeys = this.analysisService.getAllDataKeys();
  }

  onXAxisSelect = (column) => {
    if (column) {
      this.selectedXAxis = column;
    } else {
      this.selectedXAxis = null;
    }
  }

  onMapTypeSelect = (column) => {
    if (column) {
      this.selectedMapType = column;
    } else {
      this.selectedMapType = null;
    }
  }

  onColumnSelect = (column) => {
    if (column) {
      this.expression = null
      this.selectedColumn = this.config.multipleYAxis ? column : [column];
    } else {
      this.selectedColumn = null;
    }
  }

  onImageColumnSelect = (column) => {
    if (column) {
      this.selectedImageColumn = column;
    } else {
      this.selectedImageColumn = null;
    }
  }

  onDataKeySelect = (dataKey) => {
    this.resetDataKeySelection();
    if (dataKey) {
      this.selectedDataKey = dataKey;
      const dataInfo = this.analysisService.getDataInfo(new DataKey(dataKey));
      Object.keys(dataInfo.columnProperties).forEach(columnName => {
        let dataType = dataInfo.columnProperties[columnName].dataType;
        if (dataType == "NUMERICAL")
          this.numericalColumns.push(columnName);
        if (dataType == "CATEGORICAL")
          this.categoricalColumns.push(columnName);
        if (dataType == "DATE")
          this.dateColumns.push(columnName);
      })
      this.columnOrExpressionColumns = this.numericalColumns;
      if (this.config.allowCategorialInColumn)
        this.columnOrExpressionColumns = this.columnOrExpressionColumns.concat(this.categoricalColumns);
    } else {
      this.resetDataKeySelection();
    }
  }

  private resetDataKeySelection() {
    this.errorMessage = "";
    this.numericalColumns = [];
    this.categoricalColumns = [];
    this.dateColumns = [];
    this.selectedDataKey = null;

    this.selectedXAxis = null;
    this.selectedColumn = null;
    this.selectedImageColumn = null;
    this.expression = null;
    this.columnOrExpressionColumns = [];
  }

  addAnalysis() {
    if (this.config.dataKey && !this.selectedDataKey)
      this.errorMessage = "Invalid Data Key";
    else if (this.config.xAxis && !this.selectedXAxis)
      this.errorMessage = "Invalid X Axis";
    else if (this.config.imageColumn && !this.selectedImageColumn)
      this.errorMessage = "Invalid Image Column";
    else if (this.config.columnOrExpression && !this.selectedColumn && !this.expression)
      this.errorMessage = "Invalid Expression or Column";
    else if (this.config.selectedMapType && !this.selectedMapType)
      this.errorMessage = "Invalid Map Type Selected";
    else {
      let config = {};
      config["dataKey"] = this.selectedDataKey;
      config["xAxis"] = this.selectedXAxis;
      config["chartType"] = this.config.chartType;
      config["selectedColumn"] = this.selectedColumn;
      config["expression"] = this.expression;
      config["minRange"] = this.minRange;
      config["maxRange"] = this.maxRange;
      config["imageColumn"] = this.selectedImageColumn;
      config["mapType"] = this.selectedMapType;
      this.dialogRef.close(config);
    }
  }
}
