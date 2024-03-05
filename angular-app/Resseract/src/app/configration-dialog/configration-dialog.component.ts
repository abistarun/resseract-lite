import { Component, OnInit, Inject, ViewChild } from "@angular/core";
import { MatDialogRef, MAT_DIALOG_DATA, MatDialog } from "@angular/material/dialog";
import { AnalysisSpecification } from "../specs/analysis-specification";
import { ConfigKey } from "../specs/config-key";
import { CoreFetcherService } from "../services/core-fetcher/core-fetcher.service";
import { ConfigrationPropertyEditorComponent } from "../configration-property-editor/configration-property-editor.component";
import { AnalysisService } from "../services/analysis-service/analysis-service.service";
import { DataSummaryDialogComponent } from "../data-summary-dialog/data-summary-dialog.component";
import { DataKey } from "../specs/data-key";

@Component({
  selector: 'app-configration-dialog',
  templateUrl: './configration-dialog.component.html',
  styleUrls: ['./configration-dialog.component.css']
})
export class ConfigrationDialogComponent implements OnInit {

  @ViewChild('propertyEditor', { static: false }) propertyEditor: ConfigrationPropertyEditorComponent;
  analysisSpecifications: AnalysisSpecification[];
  analysisTypeVsdefaultValues: { [analysisType: string]: ConfigKey[] };
  selectedIndex: number;
  selectedAnalysisConfigKeys: ConfigKey[];
  isLoaded: boolean = false;

  constructor(public dialogRef: MatDialogRef<ConfigrationDialogComponent>,
    @Inject(MAT_DIALOG_DATA) public data: any,
    private dialog: MatDialog,
    fetcherService: CoreFetcherService,
    private analysisService: AnalysisService) {
    this.analysisSpecifications = data.analysisSpecifications;
    let analysisTypes = this.analysisSpecifications.map(spec => spec.analysisType);
    fetcherService.getRequiredConfigurations(analysisTypes).subscribe(
      values => {
        this.analysisTypeVsdefaultValues = values;
        this.isLoaded = true;
      },
      err => {
        this.isLoaded = true;
        this.dialogRef.close();
        throw err;
      }
    );
  }

  ngOnInit() {
  }

  openDataSummary(dataKey: DataKey) {
    this.dialog.open(DataSummaryDialogComponent, {
      width: '90%',
      height: '95%',
      maxWidth: '90%',
      maxHeight: '95%',
      autoFocus: false,
      data: {
        dataKey: dataKey
      }
    });
  }

  saveConfigs() {
    if (this.propertyEditor) {
      let modifiedProperties = this.propertyEditor.buildProperties();
      let properties = this.analysisSpecifications[this.selectedIndex].configurations.properties
      if (!properties)
        properties = {};
      this.analysisSpecifications[this.selectedIndex].configurations.properties = { ...properties, ...modifiedProperties };
    }
    this.dialogRef.close(this.analysisSpecifications);
  }

  onSelectAnalysis(analysisSpecSelected: AnalysisSpecification) {
    let configKeysRequired = this.analysisTypeVsdefaultValues[analysisSpecSelected.analysisType];
    this.selectedAnalysisConfigKeys = [];
    this.analysisService.fillDetailsinConfigKeys(configKeysRequired, analysisSpecSelected.dataKey, analysisSpecSelected.configurations.properties);
    configKeysRequired.forEach(key => {
      this.selectedAnalysisConfigKeys.push(key)
    });
    this.selectedIndex = this.analysisSpecifications.indexOf(analysisSpecSelected);
  }
}
