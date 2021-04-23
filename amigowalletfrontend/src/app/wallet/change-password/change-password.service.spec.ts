import { TestBed, inject } from '@angular/core/testing';

import { ChangePasswordService } from './change-password.service';
import { UriService } from "../../shared/uri.service";
import { HttpClientModule } from '@angular/common/http';

describe('ChangePasswordService', () => {
  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientModule],
      providers: [ChangePasswordService, UriService]
    });
  });

  it('should be created', inject([ChangePasswordService], (service: ChangePasswordService) => {
    expect(service).toBeTruthy();
  }));
});
