import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ConfigrationDialogComponent } from './configration-dialog.component';

describe('ConfigrationDialogComponent', () => {
  let component: ConfigrationDialogComponent;
  let fixture: ComponentFixture<ConfigrationDialogComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ ConfigrationDialogComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ConfigrationDialogComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
