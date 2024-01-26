import { Injectable } from '@angular/core';
import { CoreEventEmitterService } from '../core-event-emiter/core-event-emitter.service';

@Injectable({
  providedIn: 'root'
})
export class SliceService {

  private tabVsWidgets: {} = {};
  private currentTab: string;

  private publishers: {} = {};
  private subscribers: {} = {};

  constructor(emitterService: CoreEventEmitterService) {
    emitterService.listenDeleteWidgetEvent((widgetId) => {
      if (this.publishers[widgetId]) {
        this.publish(widgetId, null);
        this.apply(widgetId);
      }
      delete this.publishers[widgetId];
      delete this.subscribers[widgetId];
      Object.values(this.publishers).forEach(pub => pub['targets'] = pub['targets'].filter(s => s != widgetId));
    });
  }

  removePublisher(widgetId: string) {
    delete this.publishers[widgetId];
  }

  setCurrentTab(tab: string) {
    this.currentTab = tab;
  }

  registerWidget(tabId: string, widgetId: string) {
    if (!Object.keys(this.tabVsWidgets).includes(tabId))
      this.tabVsWidgets[tabId] = [];
    this.tabVsWidgets[tabId].push(widgetId);
  }

  registerPublisher(widgetId: string, targets: string[]) {
    if (!targets)
      targets = Object.keys(this.subscribers);
    this.publishers[widgetId] = {
      targets: targets
    }
  }

  registerSubscriber(widgetId: string, refetchCallback: Function, customOptions: {}, addToPublishers: boolean = true) {
    this.subscribers[widgetId] = {
      callback: refetchCallback,
      customOptions: customOptions
    }
    if (addToPublishers)
      Object.values(this.publishers).forEach(publisher => publisher['targets'].push(widgetId));
  }

  publish(widgetId: string, expression: any) {
    this.publishers[widgetId].expression = expression
  }

  apply(widgetId: string) {
    this.publishers[widgetId].targets.forEach(subscriber => {
      if (this.tabVsWidgets[this.currentTab].includes(subscriber)
        && this.tabVsWidgets[this.currentTab].includes(widgetId)
        && widgetId != subscriber)
        this.subscribers[subscriber].callback(true);
    })
  }

  getAllTargets() {
    let targets = {};
    this.tabVsWidgets[this.currentTab].forEach(widgetId => {
      if (Object.keys(this.subscribers).includes(widgetId))
        targets[widgetId] = this.subscribers[widgetId]
    })
    return targets;
  }

  getTargets(publisherId: string) {
    const publisher = this.publishers[publisherId];
    return publisher == null ? [] : publisher.targets;
  }

  updateTargets(publisherId: string, targets: string[]) {
    this.publishers[publisherId].targets = targets;
  }

  refreshAll(showLoading = true) {
    Object.keys(this.subscribers).forEach(e => {
      if (this.tabVsWidgets[this.currentTab].includes(e))
        this.subscribers[e].callback(showLoading);
    });
  }

  getExpressions(widgetId: string) {
    let expression = {}
    Object.keys(this.publishers).forEach(publisherId => {
      if (this.tabVsWidgets[this.currentTab] &&
        this.tabVsWidgets[this.currentTab].includes(publisherId) &&
        this.tabVsWidgets[this.currentTab].includes(widgetId) &&
        this.publishers[publisherId].targets.includes(widgetId)
        && widgetId != publisherId) {

        let expr = this.publishers[publisherId].expression;
        if (expr) {
          Object.keys(expr).forEach(dk => {
            if (!expression[dk]) {
              expression[dk] = expr[dk];
            } else {
              expression[dk] += "&&" + expr[dk];
            }
          })
        }
      }
    });
    return expression;
  }

  buildExpression(sliceExprConfig: any[], allowNumericalRange: boolean) {
    let expression = {};
    sliceExprConfig.forEach(config => {
      let dataType = config.dataType;
      let currExpr = ""
      if (dataType == 'NUMERICAL') {
        if (allowNumericalRange)
          currExpr += "([" + config.column + "]<=" + config.data.selectedRange[1] + ") && ([" +
            config.column + "]>=" + config.data.selectedRange[0] + ")"
        else
          currExpr += "([" + config.column + "]==" + config.data.selectedValue + ")"
      } else if (dataType == 'CATEGORICAL') {
        let selectedValues = config.data.uniqueValues.filter(v => v.selected);
        let isFirst = true;
        selectedValues.forEach(e => {
          let strEql = "([" + config.column + "]==\"" + e.name + "\")";
          if (!isFirst)
            currExpr += " || " + strEql;
          else {
            currExpr = strEql
            isFirst = false;
          }
        })
      }
      if (currExpr) {
        if (!expression[config.dataKey])
          expression[config.dataKey] = currExpr;
        else
          expression[config.dataKey] += " && (" + currExpr + ")";
      }
    });
    return expression;
  }
}
