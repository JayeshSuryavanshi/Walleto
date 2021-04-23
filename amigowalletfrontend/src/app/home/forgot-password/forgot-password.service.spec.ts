import { TestBed, inject } from '@angular/core/testing';

import { ForgotPasswordService } from './forgot-password.service';
import { UriService } from "../../shared/uri.service";
import { HttpClientModule } from '@angular/common/http';

describe('ForgotPasswordService', () => {
  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientModule],
      providers: [ForgotPasswordService, UriService]
    });
  });

  it('should be created', inject([ForgotPasswordService], (service: ForgotPasswordService) => {
    expect(service).toBeTruthy();
  }));
});
