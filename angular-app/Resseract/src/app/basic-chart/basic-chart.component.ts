import { Component, OnInit, ViewChild, ElementRef, Input } from '@angular/core';
import { chart } from 'highcharts/';

@Component({
  selector: 'app-basic-chart',
  templateUrl: './basic-chart.component.html',
  styleUrls: ['./basic-chart.component.css']
})
export class BasicChartComponent implements OnInit {

  @Input() chartType;
  @Input() title;
  @Input() description;
  @Input() xAxisData;
  @Input() xAxisLabel;
  @Input() yAxisLabel;
  @Input() yAxisData;

  @ViewChild('chartTarget', {static: true}) chartTarget: ElementRef;
  chart: Highcharts.Chart;
  constructor() { }

  ngOnInit() {
    const options: Highcharts.Options = {
      chart: {
        type: this.chartType,
        height: '370px'
      },
      title: {
        text: null
      },
      subtitle: {
        text: this.title + " " + this.description
      },
      xAxis: {
        categories: this.xAxisData,
        title: {
          text: this.xAxisLabel
        },
        crosshair: true
      },
      yAxis: {
        min: 0,
        title: {
          text: this.yAxisLabel
        }
      },
      series: [{
        name: this.yAxisLabel,
        data: this.yAxisData,
        type: undefined
      }]
    };
    this.chart = chart(this.chartTarget.nativeElement, options);
  }

}
