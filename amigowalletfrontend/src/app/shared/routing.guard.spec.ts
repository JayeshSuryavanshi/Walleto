import { TestBed, async, inject } from '@angular/core/testing';

import { RoutingGuard } from './routing.guard';
import { RouterTestingModule } from "@angular/router/testing";

describe('RoutingGuard', () => {
  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [ RouterTestingModule ],
      providers: [RoutingGuard]
    });
  });

  it('should gaurd before routing', inject([RoutingGuard], (guard: RoutingGuard) => {
    expect(guard).toBeTruthy();
  }));
});
