export class ConfigKey {
    public useDefaultValue: boolean = true;
    public currValue: any;
    public dataTypes: string;
    public allowMultiple: boolean;

    constructor(public name: string,
        public key: string,
        public defaultValue: any,
        public valueType: string,
        public possibleValues: any[],
        public editable: boolean) {
        this.currValue = defaultValue;
        this.useDefaultValue = true;
        this.allowMultiple = false;
    }
}