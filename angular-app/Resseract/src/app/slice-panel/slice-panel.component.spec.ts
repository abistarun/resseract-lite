import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { SlicePanelComponent } from './slice-panel.component';

describe('SlicePanelComponent', () => {
  let component: SlicePanelComponent;
  let fixture: ComponentFixture<SlicePanelComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ SlicePanelComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(SlicePanelComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
