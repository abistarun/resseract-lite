import { Component, OnInit, Input } from '@angular/core';
import { ConfigKey } from '../specs/config-key';

@Component({
  selector: 'app-configration-property-editor',
  templateUrl: './configration-property-editor.component.html',
  styleUrls: ['./configration-property-editor.component.css'],
})
export class ConfigrationPropertyEditorComponent implements OnInit {

  @Input() configKeys: ConfigKey[];

  constructor() {
  }

  ngOnInit() {
  }

  update = (configKey) => (value) => {
    configKey.currValue = value;
  }

  buildProperties(addDefaultValues: boolean = false) {
    let properties = {};
    this.configKeys.forEach(configKey => {
      if (addDefaultValues || !configKey.useDefaultValue) {
        properties[configKey.key] = configKey.currValue;
      }
    });
    return properties;
  }

  isAllPropertyEntered() {
    if (this.configKeys == null)
      return false;
    let isEntered = true;
    this.configKeys.forEach(configKey => {
      if (configKey.currValue == null || configKey.currValue === '') {
        isEntered = false;
      }
    });
    return isEntered;
  }
}
