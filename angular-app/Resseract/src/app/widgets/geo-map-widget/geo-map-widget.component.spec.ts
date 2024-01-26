import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { GeoMapWidgetComponent } from './geo-map-widget.component';

describe('GeoMapWidgetComponent', () => {
  let component: GeoMapWidgetComponent;
  let fixture: ComponentFixture<GeoMapWidgetComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ GeoMapWidgetComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(GeoMapWidgetComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
