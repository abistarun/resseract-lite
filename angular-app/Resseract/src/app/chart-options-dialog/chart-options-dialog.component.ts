import { Component, OnInit, Inject } from '@angular/core';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';

@Component({
  selector: 'app-chart-options-dialog',
  templateUrl: './chart-options-dialog.component.html',
  styleUrls: ['./chart-options-dialog.component.css']
})
export class ChartOptionsDialogComponent implements OnInit {
  Object = Object;
  options: {};

  constructor(@Inject(MAT_DIALOG_DATA) public data: any,
    public dialogRef: MatDialogRef<ChartOptionsDialogComponent>) {
    this.options = {}
    Object.values(data.customOptions).forEach(element => {
      let group = element["group"];
      if (!this.options[group])
        this.options[group] = [];
      this.options[group].push(element);
    });;
  }

  ngOnInit() {
  }

  saveOptions() {
  }

  isOptionValidForSeriesType(option) {
    let seriesTypeOption = this.options[option.group].filter(s => s["name"] == option.group + " Type")
    if (option.forSeriesType && seriesTypeOption.length > 0) {
      return option.forSeriesType.includes(seriesTypeOption[0].value)
    }
    return true;
  }
  update = (option) => (value) => {
    option.value = value;
  }
}
