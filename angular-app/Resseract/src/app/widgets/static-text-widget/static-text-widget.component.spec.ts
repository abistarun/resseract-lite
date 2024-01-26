import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { StaticTextWidgetComponent } from './static-text-widget.component';

describe('StaticTextWidgetComponent', () => {
  let component: StaticTextWidgetComponent;
  let fixture: ComponentFixture<StaticTextWidgetComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ StaticTextWidgetComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(StaticTextWidgetComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
