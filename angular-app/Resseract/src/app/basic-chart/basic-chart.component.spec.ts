import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { BasicChartComponent } from './basic-chart.component';

describe('BasicChartComponent', () => {
  let component: BasicChartComponent;
  let fixture: ComponentFixture<BasicChartComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ BasicChartComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(BasicChartComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
