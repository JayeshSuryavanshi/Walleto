import { TestBed } from '@angular/core/testing';

import { TransactionHistoryService } from './transaction-history.service';

describe('TransactionHistoryService', () => {
  beforeEach(() => TestBed.configureTestingModule({}));

  it('should be created', () => {
    const service: TransactionHistoryService = TestBed.get(TransactionHistoryService);
    expect(service).toBeTruthy();
  });
});
