import { Component, OnDestroy, OnInit, computed, effect, inject, signal, untracked } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router, RouterLink, RouterOutlet } from '@angular/router';
import { TranslateModule } from '@ngx-translate/core';

import { AuthService } from '../shared/auth.service';
import { UserTransaction } from '../shared/model/user-transaction';
import { IconComponent, IconName } from '../shared/ui/icon.component';
import { WordmarkComponent } from '../shared/ui/wordmark.component';
import { ThemeToggleComponent } from '../shared/ui/theme-toggle.component';
import { TransactionHistoryService } from './transaction-history/transaction-history.service';

interface QuickAction {
  link: string;
  labelKey: string;
  icon: IconName;
}

/**
 * Wallet dashboard — the signature screen.
 *
 * A sticky top bar, the balance hero (large tabular numerals with a subtle
 * count-up), four quick-action pills, and a compact list of recent
 * transactions. When a child route (add money, send, etc.) is active, the
 * dashboard yields to that screen with a back chevron to /home.
 */
@Component({
  selector: 'app-wallet',
  standalone: true,
  imports: [
    CommonModule,
    RouterOutlet,
    RouterLink,
    TranslateModule,
    IconComponent,
    WordmarkComponent,
    ThemeToggleComponent,
  ],
  templateUrl: 'wallet.component.html',
  styleUrls: ['wallet.component.css'],
})
export class WalletComponent implements OnInit, OnDestroy {
  readonly auth = inject(AuthService);
  private readonly router = inject(Router);
  private readonly txns = inject(TransactionHistoryService);

  /** True while a nested feature screen is showing in the outlet. */
  readonly childActive = signal(false);

  /** Recent transactions for the dashboard preview (reuses the history API). */
  readonly recent = signal<UserTransaction[]>([]);

  /** Animated balance used for the count-up; formatted with tabular numerals. */
  private readonly displayBalance = signal(0);
  readonly formattedBalance = computed(() =>
    new Intl.NumberFormat('en-US', { minimumFractionDigits: 2, maximumFractionDigits: 2 }).format(
      this.displayBalance(),
    ),
  );

  readonly quickActions: QuickAction[] = [
    { link: 'addMoney', labelKey: 'DASHBOARD.ADD', icon: 'plus' },
    { link: 'towallet', labelKey: 'DASHBOARD.SEND', icon: 'arrow-up-right' },
    { link: 'billpayment', labelKey: 'DASHBOARD.PAY', icon: 'receipt' },
    { link: 'banktransfer', labelKey: 'DASHBOARD.BANK', icon: 'landmark' },
  ];

  private rafId = 0;

  constructor() {
    // Count up whenever the authoritative balance changes (e.g. after refresh).
    effect(() => {
      const target = this.auth.balance();
      this.animateBalanceTo(target);
    });
  }

  ngOnInit(): void {
    // Populate/refresh the profile signal (covers page-reload with a valid token).
    this.auth.refreshProfile().subscribe({ error: () => undefined });

    // Load a small window of recent activity for the dashboard preview.
    this.txns.getAllTransactions().subscribe({
      next: (list) => {
        const sorted = [...list].sort(
          (a, b) => new Date(b.transactionDateTime).getTime() - new Date(a.transactionDateTime).getTime(),
        );
        this.recent.set(sorted.slice(0, 5));
      },
      error: () => this.recent.set([]),
    });
  }

  ngOnDestroy(): void {
    if (typeof cancelAnimationFrame !== 'undefined') {
      cancelAnimationFrame(this.rafId);
    }
  }

  onActivate(): void {
    this.childActive.set(true);
  }

  onDeactivate(): void {
    this.childActive.set(false);
  }

  isCredit(txn: UserTransaction): boolean {
    return txn.paymentType?.paymentType === 'C';
  }

  logOut(): void {
    this.auth.logout();
    this.router.navigate(['/login']);
  }

  private animateBalanceTo(target: number): void {
    const start = untracked(() => this.displayBalance());
    const reduce =
      typeof window !== 'undefined' && typeof window.matchMedia === 'function'
        ? window.matchMedia('(prefers-reduced-motion: reduce)').matches
        : false;

    if (reduce || start === target || typeof requestAnimationFrame === 'undefined') {
      this.displayBalance.set(target);
      return;
    }

    cancelAnimationFrame(this.rafId);
    const duration = 700;
    const startTime = performance.now();
    const step = (now: number): void => {
      const progress = Math.min(1, (now - startTime) / duration);
      const eased = 1 - Math.pow(1 - progress, 3);
      this.displayBalance.set(start + (target - start) * eased);
      if (progress < 1) {
        this.rafId = requestAnimationFrame(step);
      }
    };
    this.rafId = requestAnimationFrame(step);
  }
}
