import { HttpClient } from '@angular/common/http';
import { Injectable, inject } from '@angular/core';
import { Observable } from 'rxjs';

import { environment } from '../../../environments/environment';
import { MoneyTransactionResponse } from '../../shared/model/money-transaction-response';

/**
 * Redeem all reward points to wallet money. Identity derived from the JWT; body
 * is empty. `amount` is the money credited and `newBalance` the resulting wallet
 * balance.
 *   POST /RewardPointsAPI/redeemRewardPoints  body {}
 */
@Injectable({ providedIn: 'root' })
export class PointsService {
  private readonly http = inject(HttpClient);
  private readonly api = environment.apiBaseUrl;

  redeem(): Observable<MoneyTransactionResponse> {
    return this.http.post<MoneyTransactionResponse>(`${this.api}/RewardPointsAPI/redeemRewardPoints`, {});
  }
}
