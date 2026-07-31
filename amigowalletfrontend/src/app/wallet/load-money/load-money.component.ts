import { Component, OnInit, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule, FormBuilder, Validators } from '@angular/forms';
import { TranslateModule, TranslateService } from '@ngx-translate/core';

import { AuthService } from '../../shared/auth.service';
import { AmountValidator } from '../../shared/amount.validator';
import { LoggerService } from '../../shared/logger.service';
import { extractApiError, friendlyMoneyResult } from '../../shared/money-format';
import { Bank } from '../../shared/model/bank';
import { CardInfo } from '../../shared/model/card';
import { MoneyTransactionResponse } from '../../shared/model/money-transaction-response';
import { LoadMoneyService } from './load-money.service';

/**
 * Loads money into the wallet via a debit card or net banking.
 *
 * Card loads always go through the new-card entry form: the wallet-api only ever
 * exposes a MASKED card number, so a saved card can never be used to pull funds.
 * Saved cards are therefore shown for reference / removal only - to load money
 * the user types the full card number + PIN + expiry the bank will debit.
 */
@Component({
  selector: 'app-load-money',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, TranslateModule],
  templateUrl: './load-money.component.html',
  styleUrls: ['./load-money.component.css'],
})
export class LoadMoneyComponent implements OnInit {
  private readonly fb = inject(FormBuilder);
  private readonly auth = inject(AuthService);
  private readonly logger = inject(LoggerService);
  private readonly loadMoneyService = inject(LoadMoneyService);
  private readonly translate = inject(TranslateService);

  paymentType: 'debit' | 'net' = 'debit';
  cards: CardInfo[] = [];
  cardPageIndex = 0;
  cardToDelete: number | null = null;
  message: string | null = null;
  successMessage: string | null = null;
  submitted = false;
  month: number[] = [];
  year: number[] = [];
  banks: Bank[] = [];

  netForm = this.fb.group({
    loginName: ['', [Validators.required]],
    password: ['', [Validators.required]],
    amount: ['', [Validators.required, Validators.pattern('[-]?[0-9]+[.]?[0-9]{0,2}'), AmountValidator.min]],
  });

  newCardForm = this.fb.group({
    bank: ['', Validators.required],
    cardHolderName: ['', [Validators.required, Validators.pattern('[A-Za-z]+([ ][A-Za-z]+)*')]],
    debitCardNumber: ['', [Validators.required, Validators.pattern('[0-9]{16}')]],
    validMonth: ['', Validators.required],
    validYear: ['', Validators.required],
    cvv: ['', [Validators.required, Validators.pattern('[0-9]{3}')]],
    pin: ['', [Validators.required, Validators.pattern('[0-9]{4}')]],
    amount: ['', [Validators.required, Validators.pattern('[-]?[0-9]+[.]?[0-9]{0,2}'), AmountValidator.min]],
  });

  ngOnInit(): void {
    for (let i = 1; i <= 12; i++) {
      this.month.push(i);
    }
    const thisYear = new Date().getFullYear();
    for (let i = thisYear; i < thisYear + 20; i++) {
      this.year.push(i);
    }

    this.syncCards();
    this.auth.refreshProfile().subscribe({ next: () => this.syncCards(), error: () => undefined });

    this.loadMoneyService.getAllBanks().subscribe({
      next: (banks) => {
        this.banks = banks;
        this.logger.info('Successfully fetched the banks');
      },
      error: () => {
        this.logger.error('Fetching the banks failed');
        this.translate.get('ERROR_MESSAGES.BANKS_FETCH_FAILED').subscribe((value) => (this.message = value));
      },
    });
  }

  private syncCards(): void {
    this.cards = this.auth.user()?.cards ?? [];
    if (this.cardPageIndex >= this.cards.length) {
      this.cardPageIndex = Math.max(0, this.cards.length - 1);
    }
  }

  get currentCard(): CardInfo | null {
    return this.cards.length > 0 ? this.cards[this.cardPageIndex] : null;
  }

  changeType(type: 'debit' | 'net'): void {
    this.submitted = false;
    this.paymentType = type;
    this.clearMessages();
  }

  prevCard(): void {
    if (this.cardPageIndex > 0) {
      this.cardPageIndex--;
      this.clearMessages();
    }
  }

  nextCard(): void {
    if (this.cardPageIndex < this.cards.length - 1) {
      this.cardPageIndex++;
      this.clearMessages();
    }
  }

  /** Enter a brand-new card, save it, then load money from it. */
  loadMoneyByNewCard(): void {
    this.submitted = true;
    this.clearMessages();

    const month = Number(this.newCardForm.controls.validMonth.value);
    const year = Number(this.newCardForm.controls.validYear.value);
    const expiry = new Date(year, month - 1, 15).toISOString().substring(0, 10);
    const cardNumber = this.newCardForm.controls.debitCardNumber.value ?? '';
    const pin = this.newCardForm.controls.pin.value ?? '';
    const amount = Number(this.newCardForm.controls.amount.value);

    this.loadMoneyService
      .addCard({ cardNumber, expiryDate: expiry, pin, bank: Number(this.newCardForm.controls.bank.value) })
      .subscribe({
        next: () => {
          this.loadMoneyService
            .loadMoneyDebitCard({
              amount,
              cardNumber,
              pin,
              expiry,
              cardHolderName: this.newCardForm.controls.cardHolderName.value ?? '',
            })
            .subscribe({
              next: (res) => {
                this.newCardForm.reset();
                this.onLoadSuccess(res, 'Money loaded');
              },
              error: (error) => this.onError(error),
            });
        },
        error: (error) => this.onError(error),
      });
  }

  askRemove(cardId: number): void {
    this.cardToDelete = cardId;
    this.clearMessages();
  }

  cancelRemove(): void {
    this.cardToDelete = null;
  }

  confirmRemove(): void {
    const cardId = this.cardToDelete;
    this.cardToDelete = null;
    if (cardId == null) {
      return;
    }
    this.clearMessages();
    this.loadMoneyService.deleteCard(cardId).subscribe({
      next: () => {
        this.logger.info('Card removed successfully');
        this.auth.refreshProfile().subscribe({ next: () => this.syncCards(), error: () => undefined });
        this.translate.get('SUCCESS_MESSAGES.CARD_DELETE_SUCCESS').subscribe((v) => (this.successMessage = v));
      },
      error: (error) => this.onError(error),
    });
  }

  netBanking(): void {
    this.submitted = true;
    this.clearMessages();

    this.loadMoneyService
      .loadMoneyNetBanking({
        amount: Number(this.netForm.controls.amount.value),
        loginName: this.netForm.controls.loginName.value ?? '',
        password: this.netForm.controls.password.value ?? '',
      })
      .subscribe({
        next: (res) => {
          this.netForm.reset();
          this.onLoadSuccess(res, 'Money loaded via net banking');
        },
        error: (error) => this.onError(error),
      });
  }

  private onLoadSuccess(res: MoneyTransactionResponse, title: string): void {
    this.successMessage = friendlyMoneyResult(res, { title, verb: 'loaded' });
    // Instantly reflect the authoritative balance, then reconcile the full
    // profile (points total + saved cards) so this screen's list stays in sync.
    this.auth.setBalance(res.newBalance);
    this.auth.refreshProfile().subscribe({ next: () => this.syncCards(), error: () => undefined });
    this.submitted = false;
    this.logger.info(this.successMessage);
  }

  private onError(error: unknown): void {
    this.message = extractApiError(error, 'Could not complete the load. Please try again.');
    this.submitted = false;
    this.logger.error(this.message ?? 'Load failed', error);
  }

  private clearMessages(): void {
    this.message = null;
    this.successMessage = null;
  }
}
