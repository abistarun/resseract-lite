import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { SelectorWrapperComponent } from './selector-wrapper.component';

describe('SelectorWrapperComponent', () => {
  let component: SelectorWrapperComponent;
  let fixture: ComponentFixture<SelectorWrapperComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ SelectorWrapperComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(SelectorWrapperComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
