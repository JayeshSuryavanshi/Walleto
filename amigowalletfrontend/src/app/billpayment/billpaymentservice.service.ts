import { HttpClient } from '@angular/common/http';
import { Injectable, inject } from '@angular/core';
import { Observable } from 'rxjs';

import { environment } from '../../environments/environment';
import { MessageResponse } from '../shared/model/api';

/**
 * Wallet -> merchant bill payment. Payer identity comes from the JWT.
 *   GET  /WalletToMerchantTransferAPI/serviceType
 *   POST /WalletToMerchantTransferAPI/merchantType  body { serviceType }
 *   POST /WalletToMerchantTransferAPI/payBill       body { amount, merchantName }
 */
@Injectable({ providedIn: 'root' })
export class BillpaymentserviceService {
  private readonly http = inject(HttpClient);
  private readonly api = environment.apiBaseUrl;

  displayServiceType(): Observable<string[]> {
    return this.http.get<string[]>(`${this.api}/WalletToMerchantTransferAPI/serviceType`);
  }

  displayMerchantName(serviceType: string): Observable<string[]> {
    return this.http.post<string[]>(`${this.api}/WalletToMerchantTransferAPI/merchantType`, { serviceType });
  }

  payBill(amount: number, merchantName: string): Observable<MessageResponse> {
    return this.http.post<MessageResponse>(`${this.api}/WalletToMerchantTransferAPI/payBill`, { amount, merchantName });
  }
}
