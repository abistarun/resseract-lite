import { ComponentFixture, TestBed } from '@angular/core/testing';

import { DataSummaryDialogComponent } from './data-summary-dialog.component';

describe('DataSummaryDialogComponent', () => {
  let component: DataSummaryDialogComponent;
  let fixture: ComponentFixture<DataSummaryDialogComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ DataSummaryDialogComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(DataSummaryDialogComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
