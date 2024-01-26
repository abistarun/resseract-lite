import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ChartOptionsDialogComponent } from './chart-options-dialog.component';

describe('ChartOptionsDialogComponent', () => {
  let component: ChartOptionsDialogComponent;
  let fixture: ComponentFixture<ChartOptionsDialogComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ ChartOptionsDialogComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ChartOptionsDialogComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
