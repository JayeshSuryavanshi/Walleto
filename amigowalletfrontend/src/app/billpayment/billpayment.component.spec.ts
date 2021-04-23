import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { BillpaymentComponent } from './billpayment.component';

describe('BillpaymentComponent', () => {
  let component: BillpaymentComponent;
  let fixture: ComponentFixture<BillpaymentComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ BillpaymentComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(BillpaymentComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
