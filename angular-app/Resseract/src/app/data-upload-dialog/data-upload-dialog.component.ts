import { Component, OnInit, ViewChild } from '@angular/core';
import { CoreFetcherService } from '../services/core-fetcher/core-fetcher.service';
import { ConfigrationPropertyEditorComponent } from '../configration-property-editor/configration-property-editor.component';
import { Configration } from '../specs/analysis-configration';
import { ConfigKey } from '../specs/config-key';
import { MatDialogRef } from '@angular/material/dialog';
import { HttpErrorResponse } from '@angular/common/http';
import { DataKey } from '../specs/data-key';
import { ProgressIndicatorComponent } from '../progress-indicator/progress-indicator.component';
import { AnalysisService } from '../services/analysis-service/analysis-service.service';

@Component({
  selector: 'app-data-upload-dialog',
  templateUrl: './data-upload-dialog.component.html',
  styleUrls: ['./data-upload-dialog.component.css']
})
export class DataUploadDialogComponent implements OnInit {

  @ViewChild('sourceConfigPropertyEditor', { static: true }) sourceConfigPropertyEditor: ConfigrationPropertyEditorComponent;
  @ViewChild('dataConfigPropertyEditor', { static: true }) dataConfigPropertyEditor: ConfigrationPropertyEditorComponent;
  @ViewChild('dataUploadProgress', { static: false }) uploadProgressElemnent: ProgressIndicatorComponent;

  sourcesData;
  sourceTypes: string[];
  selectedSource: string;
  dataConfigKeys: ConfigKey[];
  isLoaded: boolean = false;
  errorMessage;

  constructor(private dialogRef: MatDialogRef<DataUploadDialogComponent>,
    private fetcherService: CoreFetcherService,
    private analysisService: AnalysisService) { }

  ngOnInit() {
    this.fetcherService.getAllSources().subscribe({
      next: sources => {
        this.sourcesData = sources;
        this.sourceTypes = Object.keys(this.sourcesData)
        this.isLoaded = true;
      },
      error: error => {
        close();
        throw error;
      }
    });
  }

  close() {
    this.dialogRef.close();
  }

  uploadData() {
    this.isLoaded = false;
    this.errorMessage = null;
    let config: Configration = new Configration();
    Object.assign(config.properties, this.sourceConfigPropertyEditor.buildProperties(), this.dataConfigPropertyEditor.buildProperties());
    this.fetcherService.uploadData(this.selectedSource, config).subscribe({
      next: _ => {
        setTimeout(() => this.trackProgress(new DataKey(config.properties['DATA_KEY'])), 2000);
      },
      error: error => {
        this.isLoaded = true;
        this.errorMessage = error.message;
        if (error instanceof HttpErrorResponse && error.status == 499)
          this.errorMessage = error.error.message;
      }
    });
  }

  trackProgress(dataKey: DataKey) {
    this.fetcherService.uploadDataProgress(dataKey).subscribe({
      next: progress => {
        this.uploadProgressElemnent.updateProgress(progress);
        if (progress == 100) {
          this.isLoaded = true;
          this.analysisService.updateDataInfos();
          setTimeout(() => {
            this.dialogRef.close({ showDataSummary: true, dataKey: dataKey });
          }, 500);
        } else
          setTimeout(() => this.trackProgress(dataKey), 2000);
      },
      error: error => {
        this.isLoaded = true;
        this.errorMessage = error.message;
        if (error instanceof HttpErrorResponse && error.status == 499)
          this.errorMessage = error.error.message;
      }
    });
  }

  getSourceConfigs() {
    if (this.selectedSource == null)
      return null;
    return this.sourcesData[this.selectedSource];
  }

  isSourceConfigPropertyEntered() {
    return this.sourceConfigPropertyEditor.isAllPropertyEntered();
  }

  isDataConfigPropertyEntered() {
    return this.dataConfigPropertyEditor.isAllPropertyEntered();
  }

  getDataConfigurations() {
    this.isLoaded = false;
    let config: Configration = new Configration();
    config.properties = this.sourceConfigPropertyEditor.buildProperties();
    this.fetcherService.getDataConfigurations(this.selectedSource, config).subscribe({
      next: dataConfigKeys => {
        this.dataConfigKeys = this.processDataConfigKeys(dataConfigKeys);
        this.isLoaded = true;
      },
      error: error => {
        close();
        this.isLoaded = true;
        throw error;
      }
    });
  }

  processDataConfigKeys(result: ConfigKey[]): ConfigKey[] {
    result.forEach(element => {
      element.currValue = element.defaultValue;
      element.useDefaultValue = false;
      element.defaultValue = null;
    });
    return result;
  }
}
