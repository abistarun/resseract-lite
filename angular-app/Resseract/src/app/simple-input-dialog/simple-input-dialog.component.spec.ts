import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { SimpleInputDialogComponent } from './simple-input-dialog.component';

describe('SimpleInputDialogComponent', () => {
  let component: SimpleInputDialogComponent;
  let fixture: ComponentFixture<SimpleInputDialogComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ SimpleInputDialogComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(SimpleInputDialogComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
