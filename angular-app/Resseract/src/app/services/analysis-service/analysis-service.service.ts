import { Injectable } from '@angular/core';
import { WidgetSpecification } from '../../specs/widget-specification';
import { CoreFetcherService } from '../core-fetcher/core-fetcher.service';
import { DataKey } from '../../specs/data-key';
import { ConfigKey } from '../../specs/config-key';

@Injectable({
  providedIn: 'root'
})
export class AnalysisService {

  addAnalysisFunction: Function;
  dataInfos: {} = {};
  constructor(private fetcherService: CoreFetcherService) {
  }

  public async updateDataInfos() {
    let dataInfos = await this.fetcherService.getDataInfo().toPromise()
    this.dataInfos = {};
    dataInfos.forEach(dataInfo => {
      this.dataInfos[dataInfo.dataKey.key] = dataInfo;
    });
  }

  fillDetailsinConfigKeys(configKeysRequired: ConfigKey[], dataKey: DataKey = null, userValues: {} = {}) {
    configKeysRequired.forEach(key => {
      this.fillDataInfoInConfig(key, dataKey);
      this.setDefaultValuesInConfigKeys(key, userValues[key.key])
    });
  }

  private setDefaultValuesInConfigKeys(configKey: ConfigKey, userValue: any) {
    if (!configKey.editable)
      return
    if (!configKey.defaultValue)
      configKey.useDefaultValue = false;

    if (userValue) {
      configKey.currValue = userValue;
      configKey.useDefaultValue = false;
    } else if (configKey.defaultValue) {
      configKey.currValue = configKey.defaultValue;
      configKey.useDefaultValue = true;
    }
  }

  private fillDataInfoInConfig(configKey: ConfigKey, dataKey: DataKey) {
    if (configKey.valueType == 'COLUMN_NAME' || configKey.valueType == 'COLUMN_NAMES') {
      configKey.possibleValues = [];
      const columnProps = this.getDataInfo(dataKey).columnProperties;
      Object.keys(columnProps).forEach(columnName => {
        if (configKey.dataTypes.includes(columnProps[columnName].dataType)) {
          configKey.possibleValues.push(columnName);
        }
      });
      if (configKey.valueType == 'COLUMN_NAMES') {
        configKey.allowMultiple = true;
      }
      configKey.valueType = 'LIST';
    }
  }

  public addAnalysis(widgetSpec: WidgetSpecification) {
    this.addAnalysisFunction(widgetSpec);
  }

  public registerAddAnalysisListener(listener: Function) {
    this.addAnalysisFunction = listener;
  }

  public getDataInfo(dataKey: DataKey) {
    return this.dataInfos[dataKey.key];
  }

  public getAllDataKeys(): string[] {
    return Object.keys(this.dataInfos);
  }
}
