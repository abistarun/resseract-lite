import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { AddCustomColumnDialogComponent } from './add-custom-column-dialog.component';

describe('AddCustomColumnDialogComponent', () => {
  let component: AddCustomColumnDialogComponent;
  let fixture: ComponentFixture<AddCustomColumnDialogComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ AddCustomColumnDialogComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(AddCustomColumnDialogComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
