import { HttpClient } from '@angular/common/http';
import { Injectable, inject } from '@angular/core';
import { Observable } from 'rxjs';

import { environment } from '../../../environments/environment';
import { UserProfile } from '../../shared/model/user';

/**
 * Redeem all reward points to wallet money. Identity derived from the JWT; body
 * is empty.
 *   POST /RewardPointsAPI/redeemRewardPoints  body {}
 */
@Injectable({ providedIn: 'root' })
export class PointsService {
  private readonly http = inject(HttpClient);
  private readonly api = environment.apiBaseUrl;

  redeem(): Observable<Partial<UserProfile> & { message?: string; successMessage?: string }> {
    return this.http.post<Partial<UserProfile> & { message?: string; successMessage?: string }>(
      `${this.api}/RewardPointsAPI/redeemRewardPoints`,
      {},
    );
  }
}
