import { Component, OnInit, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule, FormBuilder, Validators } from '@angular/forms';
import { TranslateModule, TranslateService } from '@ngx-translate/core';

import { AuthService } from '../../shared/auth.service';
import { AmountValidator } from '../../shared/amount.validator';
import { LoggerService } from '../../shared/logger.service';
import { Bank } from '../../shared/model/bank';
import { CardInfo } from '../../shared/model/card';
import { LoadMoneyService } from './load-money.service';

/**
 * Loads money into the wallet via a saved card, a new card, or net banking.
 * All three flows are single authenticated POSTs to the wallet-api (the bank
 * leg happens server-side) - the legacy nested browser->bank orchestration and
 * the net-banking window.location redirect are gone.
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
  addCardFlag = false;
  cards: CardInfo[] = [];
  cardPageIndex = 0;
  selectedCardId: number | null = null;
  cardToDelete: number | null = null;
  removeFlag = false;
  message: string | null = null;
  successMessage: string | null = null;
  submitted = false;
  month: number[] = [];
  year: number[] = [];
  banks: Bank[] = [];

  form = this.fb.group({
    cvv: ['', [Validators.required, Validators.pattern('[0-9]{3}')]],
    pin: ['', [Validators.required, Validators.pattern('[0-9]{4}')]],
    amount: ['', [Validators.required, Validators.pattern('[-]?[0-9]+[.]?[0-9]{0,2}'), AmountValidator.min]],
  });

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
    this.removeFlag = false;
    this.clearMessages();
  }

  prevCard(): void {
    if (this.cardPageIndex > 0) {
      this.cardPageIndex--;
      this.resetSelection();
    }
  }

  nextCard(): void {
    if (this.cardPageIndex < this.cards.length - 1) {
      this.cardPageIndex++;
      this.resetSelection();
    }
  }

  private resetSelection(): void {
    this.selectedCardId = null;
    this.clearMessages();
    this.form.reset();
  }

  addNewCardClick(): void {
    this.submitted = false;
    this.addCardFlag = true;
    this.removeFlag = false;
    this.clearMessages();
  }

  cancelAddCard(): void {
    this.addCardFlag = false;
    this.clearMessages();
  }

  selectSavedCard(cardId: number): void {
    this.submitted = false;
    this.selectedCardId = cardId;
    this.addCardFlag = false;
    this.removeFlag = false;
    this.clearMessages();
    this.form.reset();
  }

  /** Load money from the currently expanded saved card. */
  addMoney(card: CardInfo): void {
    this.submitted = true;
    this.removeFlag = false;
    this.clearMessages();

    this.loadMoneyService
      .loadMoneyDebitCard({
        amount: Number(this.form.controls.amount.value),
        // For a saved card the frontend only holds the masked number; the
        // wallet-api resolves the real card by owner + cardId server-side.
        cardNumber: card.maskedCardNumber,
        pin: this.form.controls.pin.value ?? '',
        expiry: card.expiryDate,
        cardHolderName: 'SAVED_CARD_PAYMENT',
      })
      .subscribe({
        next: () => this.onLoadSuccess('SUCCESS_MESSAGES.ADD_MONEY_SUCCESS'),
        error: (error) => this.onError(error),
      });
  }

  /** Add a brand-new card then load money from it. */
  loadMoneyByNewCard(): void {
    this.submitted = true;
    this.removeFlag = false;
    this.clearMessages();

    const month = Number(this.newCardForm.controls.validMonth.value);
    const year = Number(this.newCardForm.controls.validYear.value);
    const expiry = new Date(year, month - 1, 15).toISOString().substring(0, 10);
    const cardNumber = this.newCardForm.controls.debitCardNumber.value ?? '';
    const pin = this.newCardForm.controls.pin.value ?? '';

    this.loadMoneyService
      .addCard({ cardNumber, expiryDate: expiry, pin, bank: Number(this.newCardForm.controls.bank.value) })
      .subscribe({
        next: () => {
          this.loadMoneyService
            .loadMoneyDebitCard({
              amount: Number(this.newCardForm.controls.amount.value),
              cardNumber,
              pin,
              expiry,
              cardHolderName: this.newCardForm.controls.cardHolderName.value ?? '',
            })
            .subscribe({
              next: () => {
                this.newCardForm.reset();
                this.addCardFlag = false;
                this.onLoadSuccess('SUCCESS_MESSAGES.LOAD_CARD_SUCCESS');
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
    this.removeFlag = true;
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
    this.removeFlag = false;
    this.clearMessages();

    this.loadMoneyService
      .loadMoneyNetBanking({
        amount: Number(this.netForm.controls.amount.value),
        loginName: this.netForm.controls.loginName.value ?? '',
        password: this.netForm.controls.password.value ?? '',
      })
      .subscribe({
        next: () => {
          this.netForm.reset();
          this.onLoadSuccess('SUCCESS_MESSAGES.NET_BANKING_SUCCESS');
        },
        error: (error) => this.onError(error),
      });
  }

  private onLoadSuccess(messageKey: string): void {
    this.auth.refreshProfile().subscribe({ next: () => this.syncCards(), error: () => undefined });
    this.form.reset();
    this.translate.get(messageKey).subscribe((value) => (this.successMessage = value));
    this.submitted = false;
    this.logger.info(this.successMessage ?? 'Load success');
  }

  private onError(error: { error?: { message?: string } }): void {
    if (error?.error?.message != null) {
      this.message = error.error.message;
    } else {
      this.translate.get('ERROR_MESSAGES.SERVER_DOWN').subscribe((value) => (this.message = value));
    }
    this.submitted = false;
    this.logger.error(this.message ?? 'Load failed', error);
  }

  private clearMessages(): void {
    this.message = null;
    this.successMessage = null;
  }
}
