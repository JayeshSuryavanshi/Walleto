import { Injectable, signal } from '@angular/core';

export type Theme = 'light' | 'dark';

const STORAGE_KEY = 'walleto-theme';

/**
 * Theme state for Walleto.
 *
 * - Reads localStorage 'walleto-theme' ('light' | 'dark'); if absent, follows
 *   `prefers-color-scheme`.
 * - Toggling stamps `document.documentElement.dataset.theme` and persists.
 * - The initial attribute is set by an inline script in index.html (before
 *   first paint) so there is no flash of the wrong theme; this service simply
 *   picks up / keeps that value in sync.
 */
@Injectable({ providedIn: 'root' })
export class ThemeService {
  readonly theme = signal<Theme>(this.resolveInitial());

  constructor() {
    // Keep the DOM in sync with the resolved initial value (idempotent).
    this.apply(this.theme());
  }

  toggle(): void {
    this.set(this.theme() === 'dark' ? 'light' : 'dark');
  }

  set(theme: Theme): void {
    this.theme.set(theme);
    this.apply(theme);
    try {
      localStorage.setItem(STORAGE_KEY, theme);
    } catch {
      /* private mode / storage disabled: keep the in-memory + DOM value */
    }
  }

  private resolveInitial(): Theme {
    const stamped = document.documentElement.getAttribute('data-theme');
    if (stamped === 'light' || stamped === 'dark') {
      return stamped;
    }
    try {
      const stored = localStorage.getItem(STORAGE_KEY);
      if (stored === 'light' || stored === 'dark') {
        return stored;
      }
    } catch {
      /* ignore */
    }
    return window.matchMedia('(prefers-color-scheme: dark)').matches ? 'dark' : 'light';
  }

  private apply(theme: Theme): void {
    document.documentElement.setAttribute('data-theme', theme);
  }
}
