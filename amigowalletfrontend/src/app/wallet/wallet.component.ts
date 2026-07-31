import { Component, OnInit, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterLink, RouterOutlet } from '@angular/router';
import { TranslateModule } from '@ngx-translate/core';
import { trigger, transition, animate, style, state } from '@angular/animations';

import { AuthService } from '../shared/auth.service';
import { FeatureButton } from './feature-link';
import { ProfileComponent } from './profile/profile.component';

/**
 * Wallet dashboard shell: shows the profile bar plus the feature tiles and hosts
 * the selected feature via <router-outlet>.
 */
@Component({
  selector: 'app-wallet',
  standalone: true,
  imports: [CommonModule, RouterOutlet, RouterLink, TranslateModule, ProfileComponent],
  templateUrl: 'wallet.component.html',
  styleUrls: ['wallet.component.css'],
  animations: [
    trigger('expandCollapse', [
      state('void', style({ height: '0px', overflow: 'hidden' })),
      transition(':enter', [animate('500ms ease-in-out', style({ height: '*', overflow: 'hidden' }))]),
      transition(':leave', [animate('500ms ease-in-out', style({ height: '0px', overflow: 'hidden' }))]),
    ]),
  ],
})
export class WalletComponent implements OnInit {
  private readonly auth = inject(AuthService);

  selectedOption: number | null = null;

  /** featureName values are i18n keys, translated by the template pipe. */
  readonly featureButtons: FeatureButton[] = [
    { routerLink: 'addMoney', logoURL: 'assets/resources/images/logo1.PNG', featureName: 'HOME.ADD_MONEY_TO_WALLET' },
    { routerLink: 'redeem', logoURL: 'assets/resources/images/logo6.PNG', featureName: 'HOME.REDEEM_POINTS' },
    { routerLink: 'towallet', logoURL: 'assets/resources/images/logo2.PNG', featureName: 'HOME.WALLET_TRANSFER' },
    { routerLink: 'billpayment', logoURL: 'assets/resources/images/logo3.PNG', featureName: 'HOME.PAY_BILL' },
    { routerLink: 'banktransfer', logoURL: 'assets/resources/images/logo4.PNG', featureName: 'HOME.TRANSFER_TO_BANK' },
    { routerLink: 'viewtxn', logoURL: 'assets/resources/images/logo5.PNG', featureName: 'HOME.VIEW_TRANSACTIONS' },
    {
      routerLink: 'expenseTracking',
      logoURL: 'assets/resources/images/logo7.PNG',
      featureName: 'HOME.TRACK_EXPENSES',
    },
  ];

  ngOnInit(): void {
    // Populate/refresh the profile signal (covers page-reload with a valid token).
    this.auth.refreshProfile().subscribe({ error: () => undefined });
  }
}
