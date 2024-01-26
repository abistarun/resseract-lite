import { DataKey } from "./data-key";
import { DataProperty } from "./data-property";
import { SourceType } from "./source-type";

export class DataInfo {
	constructor(
		public dataKey: DataKey,
		public dataProperty: DataProperty,
		public sourceType: SourceType,
		public columnProperties: {}) {
	}
}