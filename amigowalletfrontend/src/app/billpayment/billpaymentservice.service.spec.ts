import { TestBed } from '@angular/core/testing';

import { BillpaymentserviceService } from './billpaymentservice.service';

describe('BillpaymentserviceService', () => {
  beforeEach(() => TestBed.configureTestingModule({}));

  it('should be created', () => {
    const service: BillpaymentserviceService = TestBed.get(BillpaymentserviceService);
    expect(service).toBeTruthy();
  });
});
