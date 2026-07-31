import { HttpClient } from '@angular/common/http';
import { Injectable, inject } from '@angular/core';
import { Observable } from 'rxjs';

import { environment } from '../../../environments/environment';

/**
 * Wallet-to-wallet transfer. Sender identity is derived from the JWT server-side;
 * the body carries only the recipient email and amount (no positional array,
 * no userId).
 *   POST /WalletToWalletAPI/transfertowallet  body { recipientEmailId, amount }
 */
@Injectable({ providedIn: 'root' })
export class WalletToWalletTransferService {
  private readonly http = inject(HttpClient);
  private readonly api = environment.apiBaseUrl;

  transfer(recipientEmailId: string, amount: number): Observable<string> {
    return this.http.post(
      `${this.api}/WalletToWalletAPI/transfertowallet`,
      { recipientEmailId, amount },
      { responseType: 'text' },
    );
  }
}
