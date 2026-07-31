import { HttpClient } from '@angular/common/http';
import { Injectable, inject } from '@angular/core';
import { Observable } from 'rxjs';

import { environment } from '../../../environments/environment';
import { Bank } from '../../shared/model/bank';
import { CardInfo } from '../../shared/model/card';
import { UserTransaction } from '../../shared/model/user-transaction';

/**
 * Load-money service. Talks ONLY to the wallet-api - the browser no longer
 * calls the bank (/EDUBank) directly; the wallet-api performs the bank leg
 * server-to-server inside a transactional boundary.
 *
 * ---- MONEY ENDPOINTS (isolated here for easy reconciliation w/ Phase-4 backend) ----
 *   POST /DebitCardAPI/loadMoneyDebitCard   body { amount, cardNumber, pin, expiry, cardHolderName? }
 *   POST /DebitCardAPI/addCard              body { cardNumber, expiryDate, pin, bank }
 *   POST /DebitCardAPI/deleteCard           body { cardId }
 *   GET  /DebitCardAPI/fetchBankDetails
 *   POST /NetBankingAPI/loadMoneyNetBanking body { amount, loginName, password }
 */
export interface LoadMoneyDebitCardRequest {
  amount: number;
  cardNumber: string;
  pin: string;
  expiry: string;
  cardHolderName?: string;
}

export interface AddCardRequest {
  cardNumber: string;
  expiryDate: string;
  pin: string;
  bank: number;
}

export interface NetBankingRequest {
  amount: number;
  loginName: string;
  password: string;
}

@Injectable({ providedIn: 'root' })
export class LoadMoneyService {
  private readonly http = inject(HttpClient);
  private readonly api = environment.apiBaseUrl;

  loadMoneyDebitCard(request: LoadMoneyDebitCardRequest): Observable<UserTransaction> {
    return this.http.post<UserTransaction>(`${this.api}/DebitCardAPI/loadMoneyDebitCard`, request);
  }

  addCard(request: AddCardRequest): Observable<CardInfo> {
    return this.http.post<CardInfo>(`${this.api}/DebitCardAPI/addCard`, request);
  }

  deleteCard(cardId: number): Observable<CardInfo> {
    return this.http.post<CardInfo>(`${this.api}/DebitCardAPI/deleteCard`, { cardId });
  }

  getAllBanks(): Observable<Bank[]> {
    return this.http.get<Bank[]>(`${this.api}/DebitCardAPI/fetchBankDetails`);
  }

  loadMoneyNetBanking(request: NetBankingRequest): Observable<UserTransaction> {
    return this.http.post<UserTransaction>(`${this.api}/NetBankingAPI/loadMoneyNetBanking`, request);
  }
}
