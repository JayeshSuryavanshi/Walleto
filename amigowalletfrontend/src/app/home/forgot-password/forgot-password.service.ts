import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable, inject } from '@angular/core';
import { Observable } from 'rxjs';

import { environment } from '../../../environments/environment';
import { ForgotPasswordResponse, MessageResponse, ResetTokenResponse } from '../../shared/model/api';

/** Account-recovery chain (all public endpoints on the wallet-api). */
@Injectable({ providedIn: 'root' })
export class ForgotPasswordService {
  private readonly http = inject(HttpClient);
  private readonly api = environment.apiBaseUrl;

  /** Step 1: returns the account's security question (never the account itself). */
  forgotPassword(emailId: string): Observable<ForgotPasswordResponse> {
    return this.http.post<ForgotPasswordResponse>(`${this.api}/ForgotPasswordAPI/forgotPassword`, { emailId });
  }

  /** Step 2: validate the answer, receiving a single-use short-lived reset token. */
  validateAnswer(emailId: string, securityAnswer: string): Observable<ResetTokenResponse> {
    return this.http.post<ResetTokenResponse>(`${this.api}/ForgotPasswordAPI/validateAnswer`, {
      emailId,
      securityAnswer,
    });
  }

  /**
   * Step 3: reset the password. The reset token is sent as a Bearer header; the
   * interceptor leaves a request that already carries Authorization untouched.
   */
  resetPassword(newPassword: string, confirmNewPassword: string, resetToken: string): Observable<MessageResponse> {
    const headers = new HttpHeaders({ Authorization: `Bearer ${resetToken}` });
    return this.http.post<MessageResponse>(
      `${this.api}/ForgotPasswordAPI/resetPassword`,
      { newPassword, confirmNewPassword },
      { headers },
    );
  }
}
