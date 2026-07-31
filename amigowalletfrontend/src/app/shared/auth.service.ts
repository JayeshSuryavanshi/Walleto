import { HttpClient } from '@angular/common/http';
import { Injectable, computed, inject, signal } from '@angular/core';
import { Observable, tap } from 'rxjs';

import { environment } from '../../environments/environment';
import { Credentials, LoginResponse } from './model/api';
import { UserProfile } from './model/user';

const TOKEN_KEY = 'aw_access_token';

/**
 * Central authentication + session state.
 *
 * - Stores ONLY the JWT access token (localStorage) - never the password.
 * - Keeps a minimal, reactive user profile (signal) sourced from the login
 *   response `user` object and refreshable via `/UserLoginAPI/getUser`.
 */
@Injectable({ providedIn: 'root' })
export class AuthService {
  private readonly http = inject(HttpClient);
  private readonly api = environment.apiBaseUrl;

  /** Reactive current-user profile. Null when logged out / not yet loaded. */
  readonly user = signal<UserProfile | null>(null);
  readonly balance = computed(() => this.user()?.balance ?? 0);
  readonly points = computed(() => this.user()?.rewardPoints ?? 0);

  getToken(): string | null {
    return localStorage.getItem(TOKEN_KEY);
  }

  /** True when a non-expired access token is present. */
  isAuthenticated(): boolean {
    const token = this.getToken();
    return token !== null && !this.isExpired(token);
  }

  login(credentials: Credentials): Observable<LoginResponse> {
    return this.http
      .post<LoginResponse>(`${this.api}/UserLoginAPI/authenticate`, credentials)
      .pipe(tap((res) => this.setSession(res)));
  }

  /** Lightweight profile refresh (replaces the legacy 20s fat-User poll). */
  refreshProfile(): Observable<UserProfile> {
    return this.http
      .post<UserProfile>(`${this.api}/UserLoginAPI/getUser`, {})
      .pipe(tap((profile) => this.user.set(profile)));
  }

  setSession(res: LoginResponse): void {
    localStorage.setItem(TOKEN_KEY, res.accessToken);
    this.user.set(res.user);
  }

  clearSession(): void {
    localStorage.removeItem(TOKEN_KEY);
    this.user.set(null);
  }

  logout(): void {
    this.clearSession();
  }

  /** Decode the JWT `exp` claim (seconds) and compare with now. */
  private isExpired(token: string): boolean {
    const payload = this.decodePayload(token);
    if (!payload || typeof payload['exp'] !== 'number') {
      // No/invalid exp claim: treat as non-expiring (server still enforces).
      return false;
    }
    return payload['exp'] * 1000 <= Date.now();
  }

  private decodePayload(token: string): Record<string, unknown> | null {
    const parts = token.split('.');
    if (parts.length !== 3) {
      return null;
    }
    try {
      const base64 = parts[1].replace(/-/g, '+').replace(/_/g, '/');
      const padded = base64.padEnd(base64.length + ((4 - (base64.length % 4)) % 4), '=');
      return JSON.parse(atob(padded)) as Record<string, unknown>;
    } catch {
      return null;
    }
  }
}
