import { HttpErrorResponse, HttpInterceptorFn } from '@angular/common/http';
import { inject } from '@angular/core';
import { Router } from '@angular/router';
import { catchError, throwError } from 'rxjs';

import { environment } from '../../environments/environment';
import { AuthService } from './auth.service';

/**
 * Attaches `Authorization: Bearer <accessToken>` to wallet-api requests and,
 * on a 401, clears the session and routes to login.
 *
 * - Only wallet-api calls get the header (asset/i18n fetches are left alone).
 * - If the request already carries an Authorization header (e.g. the
 *   forgot-password reset call using the short-lived reset token), it is left
 *   untouched.
 */
export const authInterceptor: HttpInterceptorFn = (req, next) => {
  const auth = inject(AuthService);
  const router = inject(Router);

  const isApiCall = req.url.startsWith(environment.apiBaseUrl) || req.url.includes('/AmigoWallet');

  let request = req;
  if (isApiCall && !req.headers.has('Authorization')) {
    const token = auth.getToken();
    if (token) {
      request = req.clone({ setHeaders: { Authorization: `Bearer ${token}` } });
    }
  }

  return next(request).pipe(
    catchError((error: HttpErrorResponse) => {
      if (error.status === 401 && isApiCall) {
        auth.clearSession();
        router.navigate(['/login']);
      }
      return throwError(() => error);
    }),
  );
};
