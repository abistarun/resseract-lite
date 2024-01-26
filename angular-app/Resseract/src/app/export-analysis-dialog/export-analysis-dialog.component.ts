import { Component, OnInit, Input, Inject } from '@angular/core';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { CoreFetcherService } from '../services/core-fetcher/core-fetcher.service';
import { CoreEventEmitterService } from '../services/core-event-emiter/core-event-emitter.service';

@Component({
  selector: 'app-export-analysis-dialog',
  templateUrl: './export-analysis-dialog.component.html',
  styleUrls: ['./export-analysis-dialog.component.css']
})
export class ExportAnalysisDialogComponent implements OnInit {

  specifications;
  errorMessage = ""

  constructor(public dialogRef: MatDialogRef<ExportAnalysisDialogComponent>,
    @Inject(MAT_DIALOG_DATA) public data: any,
    private fetcherService: CoreFetcherService,
    private emmiterService: CoreEventEmitterService) { }

  ngOnInit() {
    this.specifications = this.data.inputSpecifications.map(curr => [curr, false]);
  }

  exportAnalysis() {
    this.errorMessage = "";
    let specsToExport = this.specifications.filter(s => s[1]).map(s => s[0])
    if (specsToExport.length == 0) {
      this.errorMessage = "Please select atleast one analysis";
      return;
    }
    this.fetcherService.exportAsCSV(specsToExport);
    this.emmiterService.emitMessageEvent("Files will be downloaded shortly");
    this.dialogRef.close();
  }
}