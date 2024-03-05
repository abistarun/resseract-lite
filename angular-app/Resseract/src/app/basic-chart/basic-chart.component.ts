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
  @Input() color;

  @ViewChild('chartTarget', { static: true }) chartTarget: ElementRef;
  chart: Highcharts.Chart;
  constructor() { }

  ngOnInit() {
  }

  ngAfterViewInit() {
    let chartType = this.chartType;
    if (this.chartType == "histogram") {
      chartType = "column";
    }
    let chartOptions: Highcharts.Options = {
      chart: {
        type: chartType,
        zooming: {
          type: "x"
        }
      },
      title: null,
      plotOptions: {
        column: {
          groupPadding: 0,
          pointPadding: 0,
          borderWidth: 0
        }
      },
      xAxis: {
        categories: this.xAxisData
      },
      tooltip: {
        formatter: function (d) {
          if (chartType == "column") {
            let series: any = this.series
            const categories = series.xAxis.categories;
            if (series.yData[series.yData.length - 1] != 0) {
              return this.x + "<br/>Value Count: " + this.y;
            }
            const currIndex = categories.indexOf(this.x);
            const nextValue = categories[currIndex + 1];
            if (nextValue) {
              return this.x + " - " + nextValue + "<br/>Value Count: " + this.y;
            }
          } else if (chartType == "pie") {
            return this.key + " : " + this.y;
          }
        }
      },
      series: [
        {
          name: this.yAxisLabel,
          data: this.yAxisData,
          type: chartType,
          showInLegend: false,
          color: this.color
        }
      ],
      credits: {
        enabled: false
      }
    };
    if (chartType == "histogram") {
      chartOptions.plotOptions.column.groupPadding = 0;
      chartOptions.plotOptions.column.pointPadding = 0;
      chartOptions.plotOptions.column.borderWidth = 0;
    }
    setTimeout(() => {
      this.chart = chart(this.chartTarget.nativeElement, chartOptions);
    }, 500);
  }

}
