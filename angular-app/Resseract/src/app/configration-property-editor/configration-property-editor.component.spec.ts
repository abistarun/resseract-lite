import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ConfigrationPropertyEditorComponent } from './configration-property-editor.component';

describe('ConfigrationPropertyEditorComponent', () => {
  let component: ConfigrationPropertyEditorComponent;
  let fixture: ComponentFixture<ConfigrationPropertyEditorComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ ConfigrationPropertyEditorComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ConfigrationPropertyEditorComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
