import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { DataValueWidgetComponent } from './data-value-widget.component';

describe('DataValueWidgetComponent', () => {
  let component: DataValueWidgetComponent;
  let fixture: ComponentFixture<DataValueWidgetComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ DataValueWidgetComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(DataValueWidgetComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
