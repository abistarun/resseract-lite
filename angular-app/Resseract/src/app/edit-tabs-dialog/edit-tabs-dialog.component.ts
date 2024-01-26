import { Component, OnInit, Inject } from '@angular/core';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { CoreEventEmitterService } from '../services/core-event-emiter/core-event-emitter.service';
import { moveItemInArray } from '@angular/cdk/drag-drop';

@Component({
  selector: 'app-edit-tabs-dialog',
  templateUrl: './edit-tabs-dialog.component.html',
  styleUrls: ['./edit-tabs-dialog.component.css']
})
export class EditTabsDialogComponent implements OnInit {

  tabs: any;
  selectedTabIndex: number;

  constructor(public dialogRef: MatDialogRef<EditTabsDialogComponent>,
    @Inject(MAT_DIALOG_DATA) public data: any,
    private emitterService: CoreEventEmitterService) {
    this.tabs = data.tabs
    this.selectedTabIndex = data.selectedTabIndex;
  }

  ngOnInit() {
  }

  delete(i) {
    this.tabs[i].widgets.forEach(widget => {
      this.emitterService.emitDeleteWidgetEvent(widget.widgetId);
    })
    this.tabs.splice(i, 1);
  }

  drop(event) {
    moveItemInArray(this.tabs, event.previousIndex, event.currentIndex);
    if (this.selectedTabIndex == event.previousIndex)
      this.selectedTabIndex = event.currentIndex;
  }

  close() {
    this.dialogRef.close(this.selectedTabIndex);
  }
}
