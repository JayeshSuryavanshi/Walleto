import { TestBed, inject } from '@angular/core/testing';

import { PointsService } from './points.service';
import { UriService } from "../../shared/uri.service";
import { HttpClientModule } from '@angular/common/http';

describe('PointsService', () => {
  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientModule],
      providers: [PointsService, UriService]
    });
  });

  it('should be created', inject([PointsService], (service: PointsService) => {
    expect(service).toBeTruthy();
  }));
});
