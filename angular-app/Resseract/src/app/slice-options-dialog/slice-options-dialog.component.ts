import { Component, OnInit, Inject } from '@angular/core';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { SliceService } from '../services/slice-service/slice-service.service';

@Component({
  selector: 'app-slice-options-dialog',
  templateUrl: './slice-options-dialog.component.html',
  styleUrls: ['./slice-options-dialog.component.css']
})
export class SliceOptionsDialogComponent implements OnInit {

  targetLabels: {} = {};
  targets: any[] = [];

  errorMessage = ""

  constructor(public dialogRef: MatDialogRef<SliceOptionsDialogComponent>,
    @Inject(MAT_DIALOG_DATA) public data: any,
    private sliceService: SliceService) { }

  ngOnInit() {
    let allTargets = this.sliceService.getAllTargets();
    let publisherTargets = this.sliceService.getTargets(this.data.widgetId);
    Object.keys(allTargets).forEach(target => {
      if (target != this.data.widgetId) {
        this.targets.push([target, publisherTargets.includes(target)]);
        this.targetLabels[target] = allTargets[target].customOptions.title.value
      }
    })
    if (this.targets.length == 0) {
      this.errorMessage = "Please add more widgets to apply slice"
    }
  }

  done() {
    this.errorMessage = "";
    let selectedTargets = this.targets.filter(s => s[1]).map(s => s[0])
    this.dialogRef.close(selectedTargets);
  }
}
