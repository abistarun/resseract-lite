import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ManageDashboardComponent } from './manage-dashboard.component';

describe('ManageDashboardComponent', () => {
  let component: ManageDashboardComponent;
  let fixture: ComponentFixture<ManageDashboardComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ ManageDashboardComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ManageDashboardComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
