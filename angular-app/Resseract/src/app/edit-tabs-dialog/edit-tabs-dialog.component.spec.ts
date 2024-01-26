import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { EditTabsDialogComponent } from './edit-tabs-dialog.component';

describe('EditTabsDialogComponent', () => {
  let component: EditTabsDialogComponent;
  let fixture: ComponentFixture<EditTabsDialogComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ EditTabsDialogComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(EditTabsDialogComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
