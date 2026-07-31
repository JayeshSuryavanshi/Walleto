import { HttpClient } from '@angular/common/http';
import { Injectable, inject } from '@angular/core';
import { Observable } from 'rxjs';

import { environment } from '../../../environments/environment';
import { RegisterRequest } from '../../shared/model/api';
import { SecurityQuestion } from '../../shared/model/security-question';

/** Security-question lookup + final (step 2) registration (public endpoints). */
@Injectable({ providedIn: 'root' })
export class SecurityQuestionService {
  private readonly http = inject(HttpClient);
  private readonly api = environment.apiBaseUrl;

  getAllQuestions(): Observable<SecurityQuestion[]> {
    return this.http.get<SecurityQuestion[]>(`${this.api}/RegistrationAPI/getAllQuestions`);
  }

  register(data: RegisterRequest): Observable<{ userId?: number; message?: string }> {
    return this.http.post<{ userId?: number; message?: string }>(`${this.api}/RegistrationAPI/register`, data);
  }
}
