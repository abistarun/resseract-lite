import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { SliceWidgetComponent } from './slice-widget.component';

describe('SliceWidgetComponent', () => {
  let component: SliceWidgetComponent;
  let fixture: ComponentFixture<SliceWidgetComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ SliceWidgetComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(SliceWidgetComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
