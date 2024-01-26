import { Component, OnInit, Inject } from '@angular/core';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';

@Component({
  selector: 'app-metadata-dialog',
  templateUrl: './metadata-dialog.component.html',
  styleUrls: ['./metadata-dialog.component.css']
})
export class MetadataDialogComponent implements OnInit {

  analysisMetadata: any[];
  selectedElement: any;

  constructor(public dialogRef: MatDialogRef<MetadataDialogComponent>,
    @Inject(MAT_DIALOG_DATA) public data: any) {
    this.analysisMetadata = data.analysisMetadata;
  }

  onSelectMetadata(curr) {
    this.selectedElement = null;
    setTimeout(() => this.selectedElement = curr, 200)
  }

  ngOnInit() {
  }

}
