import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { BasicChartWidgetComponent } from './basic-chart-widget.component';

describe('BasicChartWidgetComponent', () => {
  let component: BasicChartWidgetComponent;
  let fixture: ComponentFixture<BasicChartWidgetComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ BasicChartWidgetComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(BasicChartWidgetComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
