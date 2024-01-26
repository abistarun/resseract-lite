import { v4 as uuid } from 'uuid';
import { Component, OnInit, HostListener, Input } from '@angular/core';
import { GridsterConfig } from 'angular-gridster2';
import { CoreEventEmitterService } from '../services/core-event-emiter/core-event-emitter.service';
import { WidgetSpecification } from '../specs/widget-specification';
import { WidgetType } from '../specs/widget-type';
import { MatDialog } from '@angular/material/dialog';
import { SimpleInputDialogComponent } from '../simple-input-dialog/simple-input-dialog.component';
import { CoreFetcherService } from '../services/core-fetcher/core-fetcher.service';
import { SliceService } from '../services/slice-service/slice-service.service';
import { EditTabsDialogComponent } from '../edit-tabs-dialog/edit-tabs-dialog.component';
import { Router } from '@angular/router';
import { AnalysisService } from '../services/analysis-service/analysis-service.service';

@Component({
  selector: 'app-analysis-dashboard',
  templateUrl: './analysis-dashboard.component.html',
  styleUrls: ['./analysis-dashboard.component.css']
})
export class AnalysisDashboardComponent implements OnInit {

  WidgetType = WidgetType;
  defaultRows = 24;
  defaultCols = 24;

  tabs = [{
    name: "Home",
    widgets: [],
    options: <GridsterConfig>this.getGridsterConfig(),
    isLoaded: false,
    id: uuid(),
  }];
  callResizeEvent: boolean = true;
  dashboardName: string;
  selectedTabIndex: number = 0;
  intervalHandle: any = null;

  constructor(private dialog: MatDialog,
    private emitterService: CoreEventEmitterService,
    private fetcherService: CoreFetcherService,
    private sliceService: SliceService,
    private analysisService: AnalysisService,
    private router: Router) {
  }

  ngOnInit() {
    this.analysisService.registerAddAnalysisListener((spec: WidgetSpecification) => this.addWidget(spec))
    this.emitterService.listenDeleteWidgetEvent(widgetId => this.removeWidget(widgetId));
    this.emitterService.listenSaveDashboardEvent((askName) => this.saveDashboard(askName));
    this.emitterService.listenLoadDashboardEvent((dashboardName) => this.loadDashboard(dashboardName));
    this.emitterService.listenNewDashboardEvent(() => this.addNewDashboard());
    this.sliceService.setCurrentTab(this.tabs[0].id);
  }

  refreshDashboard(showLoading = true) {
    this.sliceService.refreshAll(showLoading);
  }

  addNewDashboard() {
    if (this.tabs.length != 1 || this.tabs[0].widgets.length != 0) {
      var r = confirm("You have unsaved changes. Do you want to continue?");
      if (!r) {
        return
      }
    }

    this.removeAllTabs();
    this.tabs = [{
      name: "Home",
      widgets: [],
      options: <GridsterConfig>this.getGridsterConfig(),
      isLoaded: false,
      id: uuid()
    }];
    this.dashboardName = undefined;
    this.selectTab(0);
  }

  saveDashboard(askName: boolean = true) {
    if (this.tabs.length == 1 && this.tabs[0].widgets.length == 0) {
      this.emitterService.emitMessageEvent("No widgets to save");
      return
    }

    let data = this.createDashboardData();

    if (askName) {
      let dialogRef = this.dialog.open(SimpleInputDialogComponent, {
        width: '1000px',
        data: {
          type: 'TEXT',
          hint: 'Dashboard Name',
          defaultValue: this.dashboardName
        }
      });

      dialogRef.afterClosed().subscribe(value => {
        if (value) {
          this.dashboardName = value;
          this.fetcherService.saveDashboard(value, data).subscribe(() => this.emitterService.emitMessageEvent("Dashboard saved successfully!"));
        }
      });
    } else {
      let value = "Backup " + new Date().toJSON().substring(0, 19).replace('T', ' ');
      this.fetcherService.saveDashboard(value, data).subscribe(() => this.emitterService.emitMessageEvent("Dashboard saved successfully!"));
    }
  }

  @HostListener('window:beforeunload', ['$event'])
  unloadPage($event) {
    if (this.tabs.length == 1 && this.tabs[0].widgets.length == 0)
      return;
    return $event.returnValue = 'You have unsaved changes. Do you want to continue?';
  }

  loadDashboard(dashboardName: string = undefined) {
    if (dashboardName) {
      this.openDashboard(dashboardName)
      return
    }
    this.fetcherService.getAllDashboards().subscribe(dashboards => {
      let dialogRef = this.dialog.open(SimpleInputDialogComponent, {
        width: '1000px',
        data: {
          type: 'LIST',
          hint: 'Dashboard Name',
          values: dashboards
        }
      });

      dialogRef.afterClosed().subscribe(value => {
        if (value) {
          this.openDashboard(value);
        }
      });
    });
  }

  private openDashboard(value: any) {
    this.callResizeEvent = false;
    this.fetcherService.getDashoardData(value).subscribe(data => {
      this.removeAllTabs();
      data.tabs.forEach(tab => {
        let newTab = this.addTabToGrid(tab.name);
        this.addSerializedWidgets(tab.widgets, newTab);
        setTimeout(() => {
          newTab.options.api.optionsChanged();
          newTab.isLoaded = true;
        }, 1000);
      });
      this.selectTab(data.selectedIndex);
      setTimeout(() => {
        this.sliceService.refreshAll();
        this.callResizeEvent = true;
        this.dashboardName = value;
      }, 1000);
    });
  }

  private addSerializedWidgets(widgets: any, newtab: any) {
    widgets.forEach(widget => {
      let spec = new WidgetSpecification(null, widget['sourceType'], widget['type']);
      spec.x = widget['x'];
      spec.y = widget['y'];
      spec.widgetId = widget['id'];
      spec.rows = widget['rows'];
      spec.cols = widget['cols'];
      spec.serializedWidgetData = widget['data'];
      this.sliceService.registerWidget(newtab.id, spec.widgetId);
      newtab.widgets.push(spec);
    })
  }

  private removeAllTabs() {
    this.emitterService.removeAllGridResizeEvent();
    this.tabs.forEach(tab => {
      tab.widgets.forEach(widget => {
        this.emitterService.emitDeleteWidgetEvent(widget.widgetId);
      })
    });
    while (this.tabs.length != 0)
      this.tabs.pop();
  }

  private createDashboardData() {
    let tabData = this.tabs.map(tab => {
      return {
        name: tab.name,
        widgets: tab.widgets.map(widget => {
          return {
            id: widget.widgetId,
            x: widget.x,
            y: widget.y,
            rows: widget.rows,
            cols: widget.cols,
            type: widget.widgetType,
            sourceType: widget.sourceType,
            data: widget.serialize()
          }
        })
      }
    });
    return {
      selectedIndex: this.selectedTabIndex,
      tabs: tabData
    }
  }

  emitGridResizeEvent() {
    if (this.callResizeEvent) {
      this.emitterService.emitGridResizeEvent();
      this.callResizeEvent = false;
      setTimeout(() => {
        this.callResizeEvent = true;
      }, 500);
    }
  }

  editTabs() {
    let dialogRef = this.dialog.open(EditTabsDialogComponent, {
      width: '1000px',
      data: {
        tabs: this.tabs,
        selectedTabIndex: this.selectedTabIndex
      }
    });

    dialogRef.afterClosed().subscribe(selectedTabIndex => this.selectedTabIndex = selectedTabIndex);
  }

  addTab() {
    let dialogRef = this.dialog.open(SimpleInputDialogComponent, {
      width: '1000px',
      data: {
        type: 'TEXT',
        hint: 'Tab Name',
      }
    });

    dialogRef.afterClosed().subscribe(value => {
      if (value) {
        this.addTabToGrid(value);
        this.selectTab(this.tabs.length - 1);
      }
    });
  }

  private addTabToGrid(value: any) {
    const newTab = {
      name: value,
      widgets: [],
      options: <GridsterConfig>this.getGridsterConfig(),
      isLoaded: false,
      id: uuid()
    };
    this.tabs.push(newTab);
    return newTab;
  }

  onTabSelect(i: number) {
    this.selectTab(i);
    setTimeout(() => {
      this.tabs[this.selectedTabIndex].options.api.optionsChanged();
    }, 500);
  }

  private selectTab(i: number) {
    this.selectedTabIndex = i;
    this.sliceService.setCurrentTab(this.tabs[this.selectedTabIndex].id);
    if (this.tabs[this.selectedTabIndex].widgets.length > 0)
      this.sliceService.refreshAll();
  }

  removeWidget(widgetId) {
    this.tabs[this.selectedTabIndex].widgets.splice(this.tabs[this.selectedTabIndex].widgets.findIndex(item => item.widgetId == widgetId), 1);
    if (this.tabs[this.selectedTabIndex].widgets.length == 0)
      this.tabs[this.selectedTabIndex].isLoaded = false;
  }

  addWidget(spec: WidgetSpecification) {
    spec.rows = this.defaultRows;
    spec.cols = this.defaultCols;
    let origRows = this.defaultRows;
    let origCols = this.defaultRows;
    while (true) {
      if (this.tabs[this.selectedTabIndex].options.api.getNextPossiblePosition(spec) || (spec.rows == 1 && spec.cols == 1))
        break;
      let oldRows = origRows;
      let oldCols = origCols;
      if (origRows > origCols)
        origRows /= 2;
      else
        origCols /= 2;
      this.tabs[this.selectedTabIndex].widgets.forEach(widget => {
        if (widget.rows == oldRows && widget.cols == oldCols) {
          widget.rows = origRows;
          widget.cols = origCols;
        }
      })
      spec.rows = origRows;
      spec.cols = origCols;
      this.tabs[this.selectedTabIndex].options.api.optionsChanged();
    }
    if (!this.tabs[this.selectedTabIndex].options.api.getNextPossiblePosition(spec)) {
      this.emitterService.emitMessageEvent("Cannot Add widget. No space in grid.");
      return;
    }
    this.tabs[this.selectedTabIndex].widgets.push(spec);
    this.tabs[this.selectedTabIndex].isLoaded = true;
    this.sliceService.registerWidget(this.tabs[this.selectedTabIndex].id, spec.widgetId);
    this.emitGridResizeEvent();
  }

  private getGridsterConfig(): GridsterConfig {
    return {
      gridType: 'fit',
      fixedColWidth: 250,
      fixedRowHeight: 250,
      minCols: this.defaultCols,
      maxCols: this.defaultCols,
      minRows: this.defaultRows,
      maxRows: this.defaultRows,
      defaultItemCols: this.defaultCols,
      defaultItemRows: this.defaultRows,
      margin: 10,
      draggable: {
        enabled: true,
        ignoreContent: true,
      },
      resizable: {
        enabled: true
      },
      swap: true,
      itemResizeCallback: () => this.emitGridResizeEvent()
    };
  }
}
