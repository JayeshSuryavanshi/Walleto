import { TestBed, inject } from '@angular/core/testing';

import { LoginService } from './login.service';
import { UriService } from "../../shared/uri.service";
import { HttpClientModule } from '@angular/common/http';

describe('LoginService', () => {
  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientModule],
      providers: [LoginService, UriService]
    });
  });

  it('should be created', inject([LoginService], (service: LoginService) => {
    expect(service).toBeTruthy();
  }));
});
