import { Directive, Input, OnInit } from "@angular/core";
import { WidgetSpecification } from "../specs/widget-specification";
import { AnalysisSpecification } from "../specs/analysis-specification";
import { MatDialog } from "@angular/material/dialog";
import { CoreFetcherService } from "../services/core-fetcher/core-fetcher.service";
import { CoreEventEmitterService } from "../services/core-event-emiter/core-event-emitter.service";
import { SliceService } from "../services/slice-service/slice-service.service";
import { ConfigrationDialogComponent } from "../configration-dialog/configration-dialog.component";
import { ExportAnalysisDialogComponent } from "../export-analysis-dialog/export-analysis-dialog.component";
import { ChartOptionsDialogComponent } from "../chart-options-dialog/chart-options-dialog.component";
import { AnalysisService } from "../services/analysis-service/analysis-service.service";

@Directive()
export abstract class AbstractWidget implements OnInit {

    @Input() widgetSpecification: WidgetSpecification;
    loadedStatus: boolean;
    errorMessage: string = null;
    customOptions: {} = {
        title: {
            name: "Title",
            value: "",
            group: "General",
            chartPath: "title.text"
        }
    }
    specAdded: AnalysisSpecification;
    isFullscreen: boolean = false;

    constructor(protected dialog: MatDialog,
        protected fetcherService: CoreFetcherService,
        protected emiiterService: CoreEventEmitterService,
        protected sliceService: SliceService,
        protected analysisService: AnalysisService) {
    }

    ngOnInit(): void {
        this.customOptions = Object.assign(this.customOptions, this.getCustomOptions());
        this.widgetSpecification.serialize = () => this.serialize();
        if (this.widgetSpecification.serializedWidgetData) {
            this.deserialize(this.widgetSpecification.serializedWidgetData);
            return;
        }

        this.initialize(this.widgetSpecification);
    }

    ngAfterViewInit() {
        this.initializeWidgetCallbacks(this.widgetSpecification);
    }

    getWidgetId(): string {
        return this.widgetSpecification.widgetId;
    }

    initializeWidgetCallbacks(widgetSpecification: WidgetSpecification) {
        this.loadFullscreenCallback();
        this.sliceService.registerSubscriber(widgetSpecification.widgetId, (showLoading) => this.refetch(false, showLoading), this.customOptions);
        this.emiiterService.listenGridResizeEvent(() => this.reflow());
        if (!widgetSpecification.serializedWidgetData)
            this.refetch(false, true);
    }

    loadFullscreenCallback() {
        document.addEventListener('webkitfullscreenchange', () => this.toggleFullScreenFlag(), false);
        document.addEventListener('fullscreenchange', () => this.toggleFullScreenFlag(), false);
    }

    toggleFullScreenFlag() {
        this.isFullscreen = !this.isFullscreen;
    }

    enableFullScreen() {
        let el = document.getElementById(this.widgetSpecification.widgetId);
        let rfs = el.requestFullscreen;
        rfs.call(el);
    }

    deleteWidget() {
        this.emiiterService.emitDeleteWidgetEvent(this.widgetSpecification.widgetId);
    }

    configureAnalysis() {
        let dialogRef = this.dialog.open(ConfigrationDialogComponent, {
            width: '1000px',
            autoFocus: false,
            data: {
                analysisSpecifications: [this.specAdded],
            }
        });

        dialogRef.afterClosed().subscribe(specs => {
            if (specs) {
                this.specAdded = specs[0];
                this.refetch(false, true);
            }
        });
    }

    exportAsCSV() {
        this.dialog.open(ExportAnalysisDialogComponent, {
            width: '1000px',
            autoFocus: false,
            data: {
                inputSpecifications: [this.specAdded]
            }
        });
    }

    processSlice(baseSpec: AnalysisSpecification) {
        let spec = JSON.parse(JSON.stringify(baseSpec));
        let sliceExpr = spec.configurations.properties['SLICE_EXPRESSION'];
        let globalSlice = this.sliceService.getExpressions(this.widgetSpecification.widgetId)[spec.dataKey.key];
        if (globalSlice)
            if (sliceExpr) {
                spec.configurations.properties['SLICE_EXPRESSION'] = sliceExpr + "&& (" + globalSlice + ")";
            }
            else {
                spec.configurations.properties['SLICE_EXPRESSION'] = globalSlice;
            }
        return spec;
    }

    openChartOptions() {
        let dialogRef = this.dialog.open(ChartOptionsDialogComponent, {
            width: '1000px',
            autoFocus: false,
            data: {
                customOptions: this.customOptions,
            }
        });

        dialogRef.afterClosed().subscribe(_ => this.applyOptions())
    }

    convertDotNotaionToObject(key, value) {
        var object = {};
        var result = object = {};
        var arr = key.split('.');
        for (var i = 0; i < arr.length - 1; i++) {
            object = object[arr[i]] = {};
        }
        object[arr[arr.length - 1]] = value;
        return result;
    }

    applyOptionsToChartObject(chart) {
        let chartOptions = {}
        Object.values(this.customOptions).forEach(option => {
            let value = option["value"];
            if (option["chartPath"] && value) {
                if (option["transform"])
                    value = option["transform"](value);
                let currChartOption = this.convertDotNotaionToObject(option["chartPath"], value);
                if (option["chartElementId"] && chart.get(option["chartElementId"])) {
                    chart.get(option["chartElementId"]).update(currChartOption);
                } else
                    chartOptions = { ...chartOptions, ...currChartOption };
            }
        });
        chart.update(chartOptions);
    }

    abstract getCustomOptions(): {};
    abstract initialize(widgetSpecification: WidgetSpecification): void;
    abstract serialize(): void;
    abstract deserialize(serializedData: any): void;
    abstract refetch(hard: boolean, showLoading: boolean): void;
    abstract reflow(): void;
    abstract applyOptions(): void;
}