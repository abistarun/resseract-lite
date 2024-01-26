import { Component, OnInit, Inject } from '@angular/core';
import { CoreFetcherService } from '../services/core-fetcher/core-fetcher.service';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { DataKey } from '../specs/data-key';
import { CoreEventEmitterService } from '../services/core-event-emiter/core-event-emitter.service';
import { AnalysisService } from '../services/analysis-service/analysis-service.service';

@Component({
  selector: 'app-add-custom-column-dialog',
  templateUrl: './add-custom-column-dialog.component.html',
  styleUrls: ['./add-custom-column-dialog.component.css']
})
export class AddCustomColumnDialogComponent implements OnInit {

  loadedStatus: boolean = false;
  dataKey: DataKey;
  columnName: string;
  expression: string;
  errorMessage: string = "";

  constructor(private fetcherService: CoreFetcherService,
    private emitterService: CoreEventEmitterService,
    @Inject(MAT_DIALOG_DATA) public data: any,
    public dialogRef: MatDialogRef<AddCustomColumnDialogComponent>,
    private analysisService: AnalysisService
  ) {
    this.dataKey = data.dataKey;
  }

  ngOnInit() {
    this.loadedStatus = true;
  }

  addColumn() {
    this.loadedStatus = false;
    this.fetcherService.addCustomColumn(this.dataKey, this.columnName, this.expression).subscribe(
      _ => {
        this.loadedStatus = true;
        this.analysisService.updateDataInfos();
        this.emitterService.emitMessageEvent("Custom Column added to data!!");
        this.dialogRef.close();
      },
      err => {
        this.loadedStatus = true;
        this.errorMessage = err.error.message
      });
  }
}
