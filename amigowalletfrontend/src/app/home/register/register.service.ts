import { HttpClient } from '@angular/common/http';
import { Injectable, inject } from '@angular/core';
import { Observable } from 'rxjs';

import { environment } from '../../../environments/environment';
import { MessageResponse, RegisterRequest } from '../../shared/model/api';

/** Step-1 registration validation against the wallet-api (public). */
@Injectable({ providedIn: 'root' })
export class RegisterService {
  private readonly http = inject(HttpClient);
  private readonly api = environment.apiBaseUrl;

  validateForRegistration(data: RegisterRequest): Observable<MessageResponse> {
    return this.http.post<MessageResponse>(`${this.api}/RegistrationAPI/validateForRegistration`, data);
  }
}
