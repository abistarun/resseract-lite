import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { DialWidgetComponent } from './dial-widget.component';

describe('DialWidgetComponent', () => {
  let component: DialWidgetComponent;
  let fixture: ComponentFixture<DialWidgetComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ DialWidgetComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(DialWidgetComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
