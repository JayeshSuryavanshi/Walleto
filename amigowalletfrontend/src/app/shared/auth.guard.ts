import { inject } from '@angular/core';
import { CanActivateFn, Router } from '@angular/router';

import { AuthService } from './auth.service';

/**
 * Functional replacement for the legacy class-based `RoutingGuard`.
 * Allows activation only when a valid (non-expired) JWT is present; otherwise
 * clears any stale session and sends the user to the error page.
 */
export const authGuard: CanActivateFn = () => {
  const auth = inject(AuthService);
  const router = inject(Router);

  if (auth.isAuthenticated()) {
    return true;
  }

  auth.clearSession();
  return router.createUrlTree(['/error']);
};
