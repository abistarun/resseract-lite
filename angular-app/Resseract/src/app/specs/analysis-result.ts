export class AnalysisResult {
    constructor(public index: any[],
        public columns: { [columnName: string]: AnalysisElement },
        public metadata: AnalysisMetadata) { }
}

export class AnalysisMetadata {
    constructor(public elements: any[]) { }
}

export class AnalysisElement {
    constructor(public data: any[]) { }
}