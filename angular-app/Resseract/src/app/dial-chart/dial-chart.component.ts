import { Component, OnInit, ViewChild, ElementRef, Input } from '@angular/core';
import { chart } from 'highcharts/';
import * as Highcharts from 'highcharts';
import HighchartsMore from 'highcharts/highcharts-more';
HighchartsMore(Highcharts);
import HC_solid_gauge from 'highcharts/modules/solid-gauge.src';
HC_solid_gauge(Highcharts);

@Component({
  selector: 'app-dial-chart',
  templateUrl: './dial-chart.component.html',
  styleUrls: ['./dial-chart.component.css']
})
export class DialChartComponent implements OnInit {

  @ViewChild('chartTarget', {static: true}) chartTarget: ElementRef;
  chart: Highcharts.Chart;
  @Input() rangeStart;
  @Input() rangeEnd;
  @Input() value;
  @Input() higherBetter;
  @Input() title;

  constructor() {
  }

  ngOnInit() {
    const options: Highcharts.Options = {
      chart: {
        type: 'solidgauge',
        margin: [0, 0, 0, 0]
      },

      title: null,

      pane: {
        startAngle: -90,
        endAngle: 90,
        background: [{
          backgroundColor: '#FFF',
          innerRadius: '60%',
          outerRadius: '100%',
          shape: 'arc'
        }],
        size: '100%'
      },

      tooltip: {
        enabled: false
      },

      yAxis: {
        stops: [
          [0.1, this.higherBetter ? '#DF5353' : '#55BF3B'],
          [0.5, '#DDDF0D'], // yellow
          [0.8, this.higherBetter ? '#55BF3B' : '#DF5353'] 
        ],
        lineWidth: 0,
        minorTickInterval: null,
        tickAmount: 2,
        title: {
          text: this.title,
          style: {
            fontSize: '16px'
          },
          y: 50
        },
        labels: {
          y: 16
        },
        min: this.rangeStart,
        max: this.rangeEnd
      },
      plotOptions: {
        solidgauge: {
          dataLabels: {
            y: 5,
            borderWidth: 0,
            style: {
              fontSize: '16px'
            }
          }
        }
      },
      series: [{
        name: this.title,
        data: [this.value],
        type: 'solidgauge'
      }]
    };
    this.chart = chart(this.chartTarget.nativeElement, options);
  }

}
