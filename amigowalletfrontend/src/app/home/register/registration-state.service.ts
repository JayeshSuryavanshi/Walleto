import { Injectable } from '@angular/core';

import { RegisterRequest } from '../../shared/model/api';

/**
 * Holds the in-progress registration (step 1 -> step 2) in memory ONLY.
 *
 * The legacy app stashed name/email/password/mobile in sessionStorage in
 * cleartext between the two steps; keeping it in a transient service instead
 * means the password never touches disk. If the user reloads mid-flow the
 * pending data is gone and they simply restart - which is the safer default.
 */
@Injectable({ providedIn: 'root' })
export class RegistrationStateService {
  private pending: RegisterRequest | null = null;

  set(data: RegisterRequest): void {
    this.pending = data;
  }

  get(): RegisterRequest | null {
    return this.pending;
  }

  clear(): void {
    this.pending = null;
  }
}
