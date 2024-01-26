import { Component, OnInit, ViewChild } from '@angular/core';
import { MatDialogRef, MatDialog } from '@angular/material/dialog';
import { MatPaginator } from '@angular/material/paginator';
import { MatTableDataSource } from '@angular/material/table';
import { CoreFetcherService } from '../services/core-fetcher/core-fetcher.service';
import { ConfirmDialogComponent } from '../confirm-dialog/confirm-dialog.component';

@Component({
  selector: 'app-manage-dashboard',
  templateUrl: './manage-dashboard.component.html',
  styleUrls: ['./manage-dashboard.component.css']
})
export class ManageDashboardComponent implements OnInit {

  @ViewChild('paginator', { static: false }) paginator: MatPaginator;

  isLoaded = false;
  displayedColumns: string[] = ['name', 'delete'];
  dashboardData: MatTableDataSource<string>;
  constructor(private dialogRef: MatDialogRef<ManageDashboardComponent>,
    private fetcherService: CoreFetcherService,
    private dialog: MatDialog) {
  }

  ngOnInit() {
    this.fetchData();
  }

  fetchData() {
    this.isLoaded = false;
    this.fetcherService.getAllDashboards().subscribe(
      dashboards => {
        this.dashboardData = new MatTableDataSource(dashboards);
        this.dashboardData.paginator = this.paginator;
        this.isLoaded = true;
      },
      err => {
        this.dialogRef.close();
        throw err;
      });
  }

  delete(element: any) {
    let dialogRef = this.dialog.open(ConfirmDialogComponent, {
      width: '750px',
      data: {
        message: 'Are you sure you want to delete this dashboard?'
      }
    });

    dialogRef.afterClosed().subscribe(data => {
      if (data) {
        this.isLoaded = false;
        this.fetcherService.deleteDashboard(element).subscribe(() => this.fetchData());
      }
    });
  }

}
