import { Component, OnInit, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule, FormBuilder, FormGroup, Validators } from '@angular/forms';
import { TranslateModule } from '@ngx-translate/core';

import { AuthService } from '../../shared/auth.service';
import { LoggerService } from '../../shared/logger.service';
import { TransferToBankService } from './transfer-to-bank.service';

@Component({
  selector: 'app-transfer-to-bank',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, TranslateModule],
  templateUrl: './transfer-to-bank-component.component.html',
  styleUrls: ['./transfer-to-bank-component.component.css'],
})
export class TransferToBankComponent implements OnInit {
  private readonly formBuilder = inject(FormBuilder);
  private readonly transferToBankService = inject(TransferToBankService);
  private readonly logger = inject(LoggerService);
  readonly auth = inject(AuthService);

  bankAccountForm!: FormGroup;
  errorMessage: string | null = null;
  successMessage: string | null = null;
  transactionId: string | null = null;

  ngOnInit(): void {
    this.bankAccountForm = this.formBuilder.group({
      ifsc: ['', [Validators.required, Validators.pattern(/^[A-Za-z]{4}[0-9]{7}$/)]],
      accountNumber: ['', [Validators.required, Validators.pattern(/^[0-9]{15}$/)]],
      accountHolderName: ['', [Validators.required, Validators.pattern(/^[A-Za-z ]+$/)]],
      amount: ['', [Validators.required, Validators.min(1), Validators.pattern(/^[0-9]+(.[0-9]{2})?$/)]],
    });
  }

  onSubmit(): void {
    this.errorMessage = null;
    this.successMessage = null;
    this.transactionId = null;

    const amount = Number(this.bankAccountForm.controls['amount'].value);

    if (this.auth.balance() < amount) {
      this.errorMessage = 'ERROR_MESSAGES.TRANSFER_BANK_LOW_BALANCE';
      return;
    }

    this.transferToBankService
      .sendMoneyBankAccount({
        amount,
        accountNumber: this.bankAccountForm.controls['accountNumber'].value,
        ifsc: this.bankAccountForm.controls['ifsc'].value,
        accountHolderName: this.bankAccountForm.controls['accountHolderName'].value,
      })
      .subscribe({
        next: (response) => {
          this.successMessage = 'SUCCESS_MESSAGES.TRANSFER_TO_BANK_SUCCESS';
          this.transactionId = response?.message ?? '';
          this.bankAccountForm.reset();
          this.auth.refreshProfile().subscribe({ error: () => undefined });
        },
        error: (error) => {
          this.errorMessage = error?.error?.message ?? 'ERROR_MESSAGES.SERVER_DOWN';
          this.logger.error(this.errorMessage ?? 'Transfer to bank failed', error);
        },
      });
  }
}
