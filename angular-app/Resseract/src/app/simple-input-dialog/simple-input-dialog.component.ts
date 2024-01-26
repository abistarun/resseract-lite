import { Component, OnInit, Inject } from '@angular/core';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { GenericWidgetDialogComponent } from '../widgets/generic-widget-dialog/generic-widget-dialog.component';

@Component({
  selector: 'app-simple-input-dialog',
  templateUrl: './simple-input-dialog.component.html',
  styleUrls: ['./simple-input-dialog.component.css']
})
export class SimpleInputDialogComponent implements OnInit {

  inputData: any;
  selectedValue: string;
  errorMessage: string;

  constructor(@Inject(MAT_DIALOG_DATA) public data: any,
    public dialogRef: MatDialogRef<GenericWidgetDialogComponent>) {
    this.inputData = data
  }

  ngOnInit() {
    if (this.inputData.defaultValue)
      this.selectedValue = this.inputData.defaultValue
  }

  onValueSelect = (value: string) => {
    this.selectedValue = value;
  }

  done() {
    if (!this.selectedValue)
      this.errorMessage = "Invalid Input";
    else
      this.dialogRef.close(this.selectedValue);
  }
}
