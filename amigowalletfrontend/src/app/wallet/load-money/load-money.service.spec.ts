import { TestBed, inject } from '@angular/core/testing';

import { LoadMoneyService } from './load-money.service';
import { UriService } from "../../shared/uri.service";
import { HttpClientModule } from '@angular/common/http';

describe('LoadMoneyService', () => {
  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientModule],
      providers: [LoadMoneyService, UriService]
    });
  });

  it('should be created', inject([LoadMoneyService], (service: LoadMoneyService) => {
    expect(service).toBeTruthy();
  }));
});
