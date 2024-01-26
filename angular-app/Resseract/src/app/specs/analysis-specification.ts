import { Configration } from "./analysis-configration";
import { DataKey } from "./data-key";

export class AnalysisSpecification {
    public analysisName: string;
        
    constructor(public analysisType: string,
        public dataKey: DataKey,
        public configurations: Configration) {
    }
}