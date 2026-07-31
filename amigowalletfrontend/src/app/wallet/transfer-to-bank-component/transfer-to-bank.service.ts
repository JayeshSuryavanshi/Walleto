import { HttpClient } from '@angular/common/http';
import { Injectable, inject } from '@angular/core';
import { Observable } from 'rxjs';

import { environment } from '../../../environments/environment';
import { MessageResponse } from '../../shared/model/api';

export interface BankTransferRequest {
  amount: number;
  accountNumber: string;
  ifsc: string;
  accountHolderName: string;
}

/**
 * Withdraw wallet -> external bank account. The browser no longer calls the
 * bank directly (accountVerification + creditMoney are gone); the wallet-api
 * performs verify + credit + debit atomically server-side.
 *   POST /BankTrasnferAPI/sendMoneyBankAccount  body { amount, accountNumber, ifsc, accountHolderName }
 *   NOTE: mapping historically misspelled "BankTrasnfer" - confirm at reconciliation.
 */
@Injectable({ providedIn: 'root' })
export class TransferToBankService {
  private readonly http = inject(HttpClient);
  private readonly api = environment.apiBaseUrl;

  sendMoneyBankAccount(request: BankTransferRequest): Observable<MessageResponse> {
    return this.http.post<MessageResponse>(`${this.api}/BankTrasnferAPI/sendMoneyBankAccount`, request);
  }
}
