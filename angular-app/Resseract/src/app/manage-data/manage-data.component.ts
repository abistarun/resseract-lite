import { Component, OnInit, ViewChild } from '@angular/core';
import { MatDialogRef, MatDialog } from '@angular/material/dialog';
import { MatPaginator } from '@angular/material/paginator';
import { MatTableDataSource } from '@angular/material/table';
import { CoreFetcherService } from '../services/core-fetcher/core-fetcher.service';
import { DataInfo } from '../specs/data-info';
import { AddCustomColumnDialogComponent } from '../add-custom-column-dialog/add-custom-column-dialog.component';
import { ConfirmDialogComponent } from '../confirm-dialog/confirm-dialog.component';
import { AnalysisService } from '../services/analysis-service/analysis-service.service';
import { DataSummaryDialogComponent } from '../data-summary-dialog/data-summary-dialog.component';

@Component({
  selector: 'app-manage-data',
  templateUrl: './manage-data.component.html',
  styleUrls: ['./manage-data.component.css']
})
export class ManageDataComponent implements OnInit {

  @ViewChild('paginator', { static: false }) paginator: MatPaginator;

  isLoaded = false;
  displayedColumns: string[] = ['dataKey', 'sourceType', 'addColumn', 'dataSummary', 'delete'];
  dataInfo: MatTableDataSource<DataInfo>;
  constructor(private dialogRef: MatDialogRef<ManageDataComponent>,
    private fetcherService: CoreFetcherService,
    private dialog: MatDialog,
    private analysisService: AnalysisService) {
  }

  ngOnInit() {
    this.fetchDataInfo();
  }

  fetchDataInfo() {
    this.isLoaded = false;
    this.fetcherService.getDataInfo().subscribe(
      dataInfos => {
        this.dataInfo = new MatTableDataSource(dataInfos);
        this.dataInfo.paginator = this.paginator;
        this.isLoaded = true;
      },
      err => {
        this.dialogRef.close();
        throw err;
      });
  }

  openAddCustomColumnDialog(element: any) {
    this.dialog.open(AddCustomColumnDialogComponent, {
      width: '1000px',
      data: {
        dataKey: element.dataKey
      }
    });
  }

  openDataSummary(element: any) {
    this.dialog.open(DataSummaryDialogComponent, {
      width: '90%',
      height: '95%',
      maxWidth: '90%',
      maxHeight: '95%',
      autoFocus: false,
      data: {
        dataKey: element.dataKey
      }
    });
  }

  deleteData(element: any) {
    let dialogRef = this.dialog.open(ConfirmDialogComponent, {
      width: '750px',
      data: {
        message: 'All widgets in dashboards using this data will show errors. Are you sure you want to delete this data?'
      }
    });

    dialogRef.afterClosed().subscribe(data => {
      if (data) {
        this.isLoaded = false;
        this.fetcherService.deleteData(element.dataKey).subscribe(() => {
          this.fetchDataInfo();
          this.analysisService.updateDataInfos();
        });
      }
    });
  }
}
