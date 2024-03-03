export class DataSummary {
    constructor(
        public rowCount: number,
        public columnCount: number,
        public head: any[][],
        public columnStatistics: ColumnStatistics[],
    ) {
    }
}


export class ColumnStatistics {
    constructor(
        public name: string,
        public type: string,
        public uniqueValueCount: number,
        public nullValueCount: number,
        public valueCount: any[],
    ) {
    }
}