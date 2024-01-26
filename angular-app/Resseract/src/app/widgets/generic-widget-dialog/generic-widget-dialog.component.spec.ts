import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { GenericWidgetDialogComponent } from './generic-widget-dialog.component';

describe('GenericWidgetDialogComponent', () => {
  let component: GenericWidgetDialogComponent;
  let fixture: ComponentFixture<GenericWidgetDialogComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ GenericWidgetDialogComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(GenericWidgetDialogComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
