import { TestBed, inject } from '@angular/core/testing';

import { RegisterService } from './register.service';
import { UriService } from "../../shared/uri.service";
import { HttpClientModule } from '@angular/common/http';

describe('RegisterService', () => {
  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientModule],
      providers: [RegisterService, UriService]
    });
  });

  it('should be created', inject([RegisterService], (service: RegisterService) => {
    expect(service).toBeTruthy();
  }));
});
