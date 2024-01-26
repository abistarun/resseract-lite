import { state, style, trigger } from '@angular/animations';
import { Component, OnInit } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { DataUploadDialogComponent } from '../data-upload-dialog/data-upload-dialog.component';
import { ManageEntitiesComponent } from '../manage-entities/manage-entities.component';
import { AuthorisationService } from '../services/authorisation-service/authorisation-service.service';
import { CoreEventEmitterService } from '../services/core-event-emiter/core-event-emitter.service';
import { SourceType } from '../specs/source-type';
import { WidgetSpecification } from '../specs/widget-specification';
import { WidgetType } from '../specs/widget-type';
import { GenericWidgetDialogComponent } from '../widgets/generic-widget-dialog/generic-widget-dialog.component';
import { AnalysisService } from './../services/analysis-service/analysis-service.service';
@Component({
  selector: 'app-navigator',
  templateUrl: './navigator.component.html',
  styleUrls: ['./navigator.component.css'],
  animations: [
    trigger('slideInOut', [
      state('in', style({
        opacity: 1
      })),
      state('out', style({
        opacity: 0,
        display: 'none'
      })),
    ])
  ]
})
export class NavigatorComponent implements OnInit {

  dataMenuState: string = 'in';
  chartsMenuState: string = 'in';
  miscMenuState: string = 'in';

  constructor(private dialog: MatDialog,
    private emitterService: CoreEventEmitterService,
    private authService: AuthorisationService,
    private analysisService: AnalysisService) { }

  ngOnInit() {
  }

  toggleDataMenuState() {
    this.dataMenuState = this.dataMenuState == 'in' ? 'out' : 'in';
  }

  toggleChartMenuState() {
    this.chartsMenuState = this.chartsMenuState == 'in' ? 'out' : 'in';
  }

  toggleMiscMenuState() {
    this.miscMenuState = this.miscMenuState == 'in' ? 'out' : 'in';
  }

  hasFeature(feature: string) {
    return this.authService.hasFeature(feature);
  }

  addNewDashboard() {
    this.emitterService.emitNewDashboardEvent();
  }

  saveDashboard() {
    this.emitterService.emitSaveDashboardEvent(true);
  }

  loadDashboard() {
    this.emitterService.emitLoadDashboardEvent();
  }

  openPieChartDialog() {
    let config: {} = {
      dataKey: true,
      xAxis: true,
      columnOrExpression: true,
      multipleYAxis: false,
      chartType: 'pie'
    };
    let dialogRef = this.dialog.open(GenericWidgetDialogComponent, {
      width: '1000px',
      autoFocus: false,
      data: { config: config }
    });

    dialogRef.afterClosed().subscribe(config => {
      if (config) {
        let widgetSpecification: WidgetSpecification = new WidgetSpecification([], SourceType.DATA, WidgetType.PIE_CHART);
        widgetSpecification.configrations = config;
        this.analysisService.addAnalysis(widgetSpecification);
      }
    });
  }

  openBasicChartDialog(chartType) {
    let config: {} = {
      dataKey: true,
      xAxis: true,
      columnOrExpression: true,
      multipleYAxis: true,
      chartType: chartType
    };
    let dialogRef = this.dialog.open(GenericWidgetDialogComponent, {
      width: '1000px',
      autoFocus: false,
      data: { config: config }
    });

    dialogRef.afterClosed().subscribe(config => {
      if (config) {
        let widgetSpecification: WidgetSpecification = new WidgetSpecification([], SourceType.DATA, WidgetType.BASIC_CHART);
        widgetSpecification.configrations = config;
        this.analysisService.addAnalysis(widgetSpecification);
      }
    });
  }

  openDataValueWidgetDialog() {
    let config: {} = {
      dataKey: true,
      columnOrExpression: true,
      allowCategorialInColumn: true,
      multipleYAxis: false
    };
    let dialogRef = this.dialog.open(GenericWidgetDialogComponent, {
      width: '1000px',
      autoFocus: false,
      data: { config: config }
    });

    dialogRef.afterClosed().subscribe(config => {
      if (config) {
        let widgetSpecification: WidgetSpecification = new WidgetSpecification([], SourceType.DATA, WidgetType.DATA_VALUE);
        widgetSpecification.configrations = config;
        this.analysisService.addAnalysis(widgetSpecification);
      }
    });
  }

  openDataTableWidgetDialog() {
    let config: {} = {
      dataKey: true,
      xAxis: true,
      columnOrExpression: true,
      allowCategorialInColumn: true,
      multipleYAxis: true
    };
    let dialogRef = this.dialog.open(GenericWidgetDialogComponent, {
      width: '1000px',
      autoFocus: false,
      data: { config: config }
    });

    dialogRef.afterClosed().subscribe(config => {
      if (config) {
        let widgetSpecification: WidgetSpecification = new WidgetSpecification([], SourceType.DATA, WidgetType.DATA_TABLE);
        widgetSpecification.configrations = config;
        this.analysisService.addAnalysis(widgetSpecification);
      }
    });
  }

  openGeoMapWidgetDialog() {
    let config: {} = {
      dataKey: true,
      xAxis: true,
      columnOrExpression: true,
      allowCategorialInColumn: true,
      multipleYAxis: false,
      mapType: true
    };
    let dialogRef = this.dialog.open(GenericWidgetDialogComponent, {
      width: '1000px',
      autoFocus: false,
      data: { config: config }
    });

    dialogRef.afterClosed().subscribe(config => {
      if (config) {
        let widgetSpecification: WidgetSpecification = new WidgetSpecification([], SourceType.DATA, WidgetType.GEO_MAP);
        widgetSpecification.configrations = config;
        this.analysisService.addAnalysis(widgetSpecification);
      }
    });
  }

  openDialWidgetDialog() {
    let config: {} = {
      dataKey: true,
      columnOrExpression: true,
      multipleYAxis: false,
      range: true
    };
    let dialogRef = this.dialog.open(GenericWidgetDialogComponent, {
      width: '1000px',
      autoFocus: false,
      data: { config: config }
    });

    dialogRef.afterClosed().subscribe(config => {
      if (config) {
        let widgetSpecification: WidgetSpecification = new WidgetSpecification([], SourceType.DATA, WidgetType.DIAL);
        widgetSpecification.configrations = config;
        this.analysisService.addAnalysis(widgetSpecification);
      }
    });
  }

  openImageWidgetDialog() {
    let config: {} = {
      dataKey: true,
      imageColumn: true
    };
    let dialogRef = this.dialog.open(GenericWidgetDialogComponent, {
      width: '1000px',
      autoFocus: false,
      data: { config: config }
    });

    dialogRef.afterClosed().subscribe(config => {
      if (config) {
        let widgetSpecification: WidgetSpecification = new WidgetSpecification([], SourceType.DATA, WidgetType.IMAGE);
        widgetSpecification.configrations = config;
        this.analysisService.addAnalysis(widgetSpecification);
      }
    });
  }

  addStaticTextWidget() {
    let widgetSpecification: WidgetSpecification = new WidgetSpecification(null, SourceType.DATA, WidgetType.STATIC_TEXT);
    this.analysisService.addAnalysis(widgetSpecification);
  }

  addSliceWidget() {
    let widgetSpecification: WidgetSpecification = new WidgetSpecification(null, SourceType.DATA, WidgetType.SLICE);
    this.analysisService.addAnalysis(widgetSpecification);
  }

  openUploadDataDialog() {
    this.dialog.open(DataUploadDialogComponent, {
      width: '1000px',
    });
  }

  openManageDataDialog() {
    this.dialog.open(ManageEntitiesComponent, {
      width: '1000px',
    });
  }
}
