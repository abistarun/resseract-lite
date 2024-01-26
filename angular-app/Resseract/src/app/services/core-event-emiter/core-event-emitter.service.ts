import { Injectable } from '@angular/core';
import { EventEmitter } from 'events';

@Injectable()
export class CoreEventEmitterService {

  gridResizeEvent: EventEmitter = new EventEmitter();
  deleteWidget: EventEmitter = new EventEmitter();
  messageEvent: EventEmitter = new EventEmitter();
  saveDashboardEvent: EventEmitter = new EventEmitter();
  loadDashboardEvent: EventEmitter = new EventEmitter();
  newDashboardEvent: EventEmitter = new EventEmitter();

  constructor() {
    this.gridResizeEvent.setMaxListeners(100);
  }

  emitSaveDashboardEvent(askName: boolean): void {
    this.saveDashboardEvent.emit('dummy', askName);
  }

  listenSaveDashboardEvent(callback): void {
    this.saveDashboardEvent.addListener('dummy', callback);
  }

  emitNewDashboardEvent(): void {
    this.newDashboardEvent.emit('dummy');
  }

  listenNewDashboardEvent(callback): void {
    this.newDashboardEvent.addListener('dummy', callback);
  }

  emitLoadDashboardEvent(dashboardName: string = undefined): void {
    this.loadDashboardEvent.emit('dummy', dashboardName);
  }

  listenLoadDashboardEvent(callback): void {
    this.loadDashboardEvent.addListener('dummy', callback);
  }

  emitGridResizeEvent(): void {
    this.gridResizeEvent.emit('dummy');
  }

  listenGridResizeEvent(callback): void {
    this.gridResizeEvent.addListener('dummy', callback);
  }

  removeAllGridResizeEvent(): void {
    this.gridResizeEvent.removeAllListeners('dummy');
  }

  emitDeleteWidgetEvent(widgetId): void {
    this.deleteWidget.emit('dummy', widgetId);
  }

  listenDeleteWidgetEvent(callback): void {
    this.deleteWidget.addListener('dummy', callback);
  }

  emitMessageEvent(message): void {
    this.messageEvent.emit('dummy', message);
  }

  listenMessageEvent(callback): void {
    this.messageEvent.addListener('dummy', callback);
  }
}
