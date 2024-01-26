import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { DataUploadDialogComponent } from './data-upload-dialog.component';

describe('DataUploadDialogComponent', () => {
  let component: DataUploadDialogComponent;
  let fixture: ComponentFixture<DataUploadDialogComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ DataUploadDialogComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(DataUploadDialogComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
