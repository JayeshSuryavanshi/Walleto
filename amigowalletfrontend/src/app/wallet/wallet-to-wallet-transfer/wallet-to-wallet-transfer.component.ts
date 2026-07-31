import { Component, OnInit, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule, FormBuilder, FormGroup, Validators } from '@angular/forms';
import { TranslateModule, TranslateService } from '@ngx-translate/core';

import { AuthService } from '../../shared/auth.service';
import { AmountValidator } from '../../shared/amount.validator';
import { LoggerService } from '../../shared/logger.service';
import { WalletToWalletTransferService } from './wallet-to-wallet-transfer.service';

@Component({
  selector: 'app-wallet-to-wallet-transfer',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, TranslateModule],
  templateUrl: './wallet-to-wallet-transfer.component.html',
  styleUrls: ['./wallet-to-wallet-transfer.component.css'],
})
export class WalletToWalletTransferComponent implements OnInit {
  private readonly fb = inject(FormBuilder);
  private readonly service = inject(WalletToWalletTransferService);
  private readonly logger = inject(LoggerService);
  private readonly auth = inject(AuthService);
  private readonly translate = inject(TranslateService);

  tranfertowallet!: FormGroup;
  successMessage: string | null = null;
  errorMessage: string | null = null;
  submitted = false;

  ngOnInit(): void {
    this.tranfertowallet = this.fb.group({
      emailid: ['', [Validators.required, Validators.pattern('[^@]+[@][^@]+[.][^@]+')]],
      transferamount: ['', [Validators.required, AmountValidator.min, Validators.pattern('^(\\d)*(.[\\d]{1,2})?$')]],
    });
  }

  transfer(): void {
    this.successMessage = null;
    this.errorMessage = null;

    const recipientEmailId: string = this.tranfertowallet.get('emailid')?.value ?? '';
    const amount = Number(this.tranfertowallet.get('transferamount')?.value);

    if (this.auth.user()?.emailId === recipientEmailId) {
      this.translate
        .get('ERROR_MESSAGES.SELF_WALLET_TRANSFER_ERROR')
        .subscribe((value) => (this.errorMessage = value));
      return;
    }

    if (this.auth.balance() < amount) {
      const num = (Math.round(this.auth.balance() * 100) / 100).toFixed(2);
      this.translate
        .get('ERROR_MESSAGES.TRANSFER_BANK_LOW_BALANCE', { value: num })
        .subscribe((value) => (this.errorMessage = value));
      return;
    }

    this.submitted = true;
    this.service.transfer(recipientEmailId, amount).subscribe({
      next: (response) => {
        this.successMessage = response;
        this.submitted = false;
        this.logger.info('Transaction success');
        this.tranfertowallet.reset();
        this.auth.refreshProfile().subscribe({ error: () => undefined });
      },
      error: (error) => {
        this.submitted = false;
        this.errorMessage = this.extractMessage(error);
        this.logger.error(this.errorMessage ?? 'Transfer failed', error);
      },
    });
  }

  private extractMessage(error: { error?: unknown }): string {
    const body = error?.error;
    if (typeof body === 'string') {
      try {
        const parsed = JSON.parse(body) as { message?: string };
        return parsed.message ?? body;
      } catch {
        return body;
      }
    }
    if (body && typeof body === 'object' && 'message' in body) {
      return (body as { message?: string }).message ?? 'Transfer failed';
    }
    return 'Transfer failed';
  }
}
