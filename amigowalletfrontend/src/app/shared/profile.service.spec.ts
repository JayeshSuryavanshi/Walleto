import { TestBed, inject } from '@angular/core/testing';

import { ProfileService } from './profile.service';
import { HttpClientModule } from '@angular/common/http';
import { UriService } from './uri.service';

describe('ProfileService', () => {
  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientModule],
      providers: [ProfileService, UriService]
    });
  });

  it('should be created', inject([ProfileService], (service: ProfileService) => {
    expect(service).toBeTruthy();
  }));
});
