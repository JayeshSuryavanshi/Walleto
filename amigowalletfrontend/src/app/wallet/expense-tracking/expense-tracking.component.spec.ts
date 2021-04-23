import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ExpenseTrackingComponent } from './expense-tracking.component';

describe('ExpenseTrackingComponent', () => {
  let component: ExpenseTrackingComponent;
  let fixture: ComponentFixture<ExpenseTrackingComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ ExpenseTrackingComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ExpenseTrackingComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
