import { Injectable, isDevMode } from '@angular/core';

/**
 * Tiny console-backed logger that replaces the abandoned `ng2-logger` dependency.
 * Info logs are suppressed outside of dev mode; warnings and errors always print.
 */
@Injectable({ providedIn: 'root' })
export class LoggerService {
  info(message: string, obj?: unknown): void {
    if (!isDevMode()) {
      return;
    }
    obj !== undefined ? console.info('[AmigoWallet]', message, obj) : console.info('[AmigoWallet]', message);
  }

  warn(message: string, obj?: unknown): void {
    obj !== undefined ? console.warn('[AmigoWallet]', message, obj) : console.warn('[AmigoWallet]', message);
  }

  error(message: string, obj?: unknown): void {
    obj !== undefined ? console.error('[AmigoWallet]', message, obj) : console.error('[AmigoWallet]', message);
  }
}
