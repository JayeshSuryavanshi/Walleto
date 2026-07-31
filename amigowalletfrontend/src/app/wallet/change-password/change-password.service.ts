import { HttpClient } from '@angular/common/http';
import { Injectable, inject } from '@angular/core';
import { Observable } from 'rxjs';

import { environment } from '../../../environments/environment';
import { MessageResponse } from '../../shared/model/api';

export interface ChangePasswordRequest {
  password: string;
  newPassword: string;
  confirmNewPassword: string;
}

/**
 * Change the logged-in user's password (identity from the JWT, no userId).
 *   POST /UserLoginAPI/customerChangePassword  body { password, newPassword, confirmNewPassword }
 */
@Injectable({ providedIn: 'root' })
export class ChangePasswordService {
  private readonly http = inject(HttpClient);
  private readonly api = environment.apiBaseUrl;

  changePassword(request: ChangePasswordRequest): Observable<MessageResponse> {
    return this.http.post<MessageResponse>(`${this.api}/UserLoginAPI/customerChangePassword`, request);
  }
}
