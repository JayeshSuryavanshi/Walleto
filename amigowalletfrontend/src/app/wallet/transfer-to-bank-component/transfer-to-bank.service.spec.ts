import { TestBed } from '@angular/core/testing';

import { TransferToBankService } from './transfer-to-bank.service';

describe('TransferToBankService', () => {
  beforeEach(() => TestBed.configureTestingModule({}));

  it('should be created', () => {
    const service: TransferToBankService = TestBed.get(TransferToBankService);
    expect(service).toBeTruthy();
  });
});
