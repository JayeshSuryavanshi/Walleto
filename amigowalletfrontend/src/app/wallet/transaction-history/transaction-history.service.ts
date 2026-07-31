import { HttpClient } from '@angular/common/http';
import { Injectable, inject } from '@angular/core';
import { Observable } from 'rxjs';

import { environment } from '../../../environments/environment';
import { UserTransaction } from '../../shared/model/user-transaction';

/**
 * Full transaction history for the logged-in user (identity from the JWT).
 *   POST /TransactionHistoryAPI/getAllTransactions  body {}
 */
@Injectable({ providedIn: 'root' })
export class TransactionHistoryService {
  private readonly http = inject(HttpClient);
  private readonly api = environment.apiBaseUrl;

  getAllTransactions(): Observable<UserTransaction[]> {
    return this.http.post<UserTransaction[]>(`${this.api}/TransactionHistoryAPI/getAllTransactions`, {});
  }
}
