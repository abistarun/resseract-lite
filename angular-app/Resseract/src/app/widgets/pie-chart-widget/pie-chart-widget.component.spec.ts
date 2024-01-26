import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { PieChartWidgetComponent } from './pie-chart-widget.component';

describe('PieChartWidgetComponent', () => {
  let component: PieChartWidgetComponent;
  let fixture: ComponentFixture<PieChartWidgetComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ PieChartWidgetComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(PieChartWidgetComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
