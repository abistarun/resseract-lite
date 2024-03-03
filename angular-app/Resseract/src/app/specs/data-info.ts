import { Configration } from "./analysis-configration";
import { DataKey } from "./data-key";
import { SourceType } from "./source-type";

export class DataInfo {
	constructor(
		public dataKey: DataKey,
		public config: Configration,
		public sourceType: SourceType,
		public columnProperties: {}) {
	}
}