import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { SliceOptionsDialogComponent } from './slice-options-dialog.component';

describe('SliceOptionsDialogComponent', () => {
  let component: SliceOptionsDialogComponent;
  let fixture: ComponentFixture<SliceOptionsDialogComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ SliceOptionsDialogComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(SliceOptionsDialogComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
