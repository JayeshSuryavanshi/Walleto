import { Component, OnInit, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule, FormBuilder, FormGroup, Validators } from '@angular/forms';
import { TranslateModule } from '@ngx-translate/core';

import { AuthService } from '../../shared/auth.service';
import { LoggerService } from '../../shared/logger.service';
import { extractApiError, friendlyMoneyResult } from '../../shared/money-format';
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
          this.successMessage = friendlyMoneyResult(response, {
            title: 'Sent to bank',
            verb: 'transferred',
          });
          this.bankAccountForm.reset();
          this.auth.applyMoneyResult(response);
        },
        error: (error) => {
          this.errorMessage = extractApiError(error, 'Transfer to bank failed. Please try again.');
          this.logger.error(this.errorMessage ?? 'Transfer to bank failed', error);
        },
      });
  }
}
