import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { DataTableWidgetComponent } from './data-table-widget.component';

describe('DataTableWidgetComponent', () => {
  let component: DataTableWidgetComponent;
  let fixture: ComponentFixture<DataTableWidgetComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ DataTableWidgetComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(DataTableWidgetComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
