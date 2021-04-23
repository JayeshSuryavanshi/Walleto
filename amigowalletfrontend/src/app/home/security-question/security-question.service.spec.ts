import { TestBed, inject } from '@angular/core/testing';

import { SecurityQuestionService } from './security-question.service';
import { HttpClientModule } from '@angular/common/http';
import { UriService } from 'src/app/shared/uri.service';

describe('SecurityQuestionService', () => {
  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientModule],
      providers: [SecurityQuestionService, UriService]
    });
  });

  it('should be created', inject([SecurityQuestionService], (service: SecurityQuestionService) => {
    expect(service).toBeTruthy();
  }));
});
