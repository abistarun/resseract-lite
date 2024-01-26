import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ExportAnalysisDialogComponent } from './export-analysis-dialog.component';

describe('ExportAnalysisDialogComponent', () => {
  let component: ExportAnalysisDialogComponent;
  let fixture: ComponentFixture<ExportAnalysisDialogComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ ExportAnalysisDialogComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ExportAnalysisDialogComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
