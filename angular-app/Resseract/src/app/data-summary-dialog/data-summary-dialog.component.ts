import { Component, Inject, OnInit } from '@angular/core';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { AnalysisService } from '../services/analysis-service/analysis-service.service';
import { CoreFetcherService } from '../services/core-fetcher/core-fetcher.service';
import { DataKey } from '../specs/data-key';
import { DataSummary } from '../specs/data-summary';
import { DataInfo } from '../specs/data-info';
import { DatePipe } from '@angular/common';

@Component({
  selector: 'data-summary-dialog',
  templateUrl: './data-summary-dialog.component.html',
  styleUrls: ['./data-summary-dialog.component.css']
})
export class DataSummaryDialogComponent implements OnInit {

  isLoaded: boolean = false;
  dataKey: DataKey;
  dataSpecificationTableData: any[][];
  dataSpecificationTableHeader: string[];
  dataConfigTableData: any[][];
  dataConfigTableHeader: string[];
  sampleData: any[][];
  sampleDataHeader: string[];
  columnStatisticsData: any[];
  loadColumnStatistics: boolean = false;
  dataPipe = new DatePipe("en-US");

  constructor(@Inject(MAT_DIALOG_DATA) private data: any,
    private dialogRef: MatDialogRef<DataSummaryDialogComponent>,
    private fetcherService: CoreFetcherService,
    private analysisService: AnalysisService) {
    this.dataKey = data.dataKey;
  }

  ngOnInit(): void {
    this.fetcherService.getDataSummary(this.dataKey).subscribe({
      next: dataSummary => {
        let dataInfo: DataInfo = this.analysisService.getDataInfo(this.dataKey);
        this.prepareDataSpecificationData(dataInfo, dataSummary);
        this.prepareDataConfigData(dataInfo);
        this.prepareSampleData(dataSummary);
        this.prepareColumnStatisticsData(dataSummary, dataInfo)
        this.isLoaded = true;
      },
      error: error => {
        this.dialogRef.close();
        throw error;
      }
    });
  }

  loadData(selectedTab: number) {
    if (selectedTab == 1) {
      this.loadColumnStatistics = true;
    }
  }

  prepareDataSpecificationData(dataInfo: DataInfo, dataSummary: DataSummary) {
    this.dataSpecificationTableHeader = ["Property", "Value"];
    this.dataSpecificationTableData = [
      ["Data Key", dataInfo.dataKey.key],
      ["Source Type", dataInfo.sourceType],
      ["Number of Rows", dataSummary.rowCount],
      ["Number of Columns", dataSummary.columnCount]
    ]
  }

  prepareDataConfigData(dataInfo: DataInfo) {
    this.dataConfigTableHeader = ["Property", "Value"];
    this.dataConfigTableData = [];
    let properties = dataInfo.config.properties;
    Object.keys(properties).forEach(x => {
      if (x != "DATA_KEY") {
        this.dataConfigTableData.push([x, properties[x]]);
      }
    });
  }

  prepareSampleData(dataSummary: DataSummary) {
    this.sampleDataHeader = dataSummary.columnStatistics.map(x => x.name);
    this.sampleData = dataSummary.head;
  }

  prepareColumnStatisticsData(dataSummary: DataSummary, dataInfo: DataInfo) {
    this.columnStatisticsData = dataSummary.columnStatistics.map(stat => {
      let data: any;
      if (stat.type == "CATEGORICAL") {
        data = Object.keys(stat.valueCount).sort(function (a, b) { return stat.valueCount[b] - stat.valueCount[a] }).map(x => [x, stat.valueCount[x]]);
      } else if (stat.type == "NUMERICAL") {
        data = Object.keys(stat.valueCount).map(x => [x, +x]).sort((a: any, b: any) => a[1] - b[1]);
        data = {
          xAxisData: data.map(x => x[1]),
          yAxisData: data.map(x => stat.valueCount[x[0]]),
        }
      } else if (stat.type == "DATE") {
        data = Object.keys(stat.valueCount).map(x => [x, new Date(x)]).sort((a: any, b: any) => a[1] - b[1]);
        data = {
          xAxisData: data.map(x => this.dataPipe.transform(x[1], dataInfo.config.properties[stat.name + " Format"])),
          yAxisData: data.map(x => stat.valueCount[x[0]]),
        }
      } else if (stat.type == "BOOLEAN") {
        data = {
          yAxisData: Object.keys(stat.valueCount).map(x => {
            return {
              name: x,
              y: stat.valueCount[x]
            }
          }),
        }
      }
      return {
        name: stat.name,
        type: stat.type,
        header: ["Value", "Count"],
        data: data,
        uniqueValueCount: stat.uniqueValueCount,
        nullValueCount: stat.nullValueCount
      }
    });
  }
}

