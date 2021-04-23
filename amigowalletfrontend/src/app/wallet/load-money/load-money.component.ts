
import { Component, OnInit } from '@angular/core';
import { FormBuilder, Validators } from '@angular/forms';
import { TranslateService } from '@ngx-translate/core';

import { Bank } from '../../shared/model/bank';
import { Card } from '../../shared/model/card';
import { User } from '../../shared/model/user';
import { UserTransaction } from '../../shared/model/user-transaction';

import { AmountValidator } from '../../shared/amount.validator';
import { LoggerService } from '../../shared/logger.service';

import { ProfileService } from '../../shared/profile.service';
import { UriService } from '../../shared/uri.service';

import { LoadMoneyService } from './load-money.service';


/** This component provides the provision for loading money from saved card, 
 * new card or by internet banking */

/** Annotation which specifes this as a component 
 * 
 * moduleId: This specified for specifing that the path used in the component are relative to this component
 * templateUrl: This is the relative path for the html related to this component
 * styleUrls: This is the relative path for the css related to this component
 * providers: specifies the service class which communicates with the Serverside application
 *            specifing here makes it available only in this class
 * animations: specifing the animation for the component
*/
@Component({
  templateUrl: './load-money.component.html',
  styleUrls: ['./load-money.component.css'],
  providers: [LoadMoneyService],
})
export class LoadMoneyComponent implements OnInit {
  removeFlag: boolean;

  /** class instance variales */
  paymentType = 'debit';
  addCardFlag = false;
  cards: Card[];
  selectedCard: string;
  message: string;
  user: User;
  error: any;
  successMessage: string;
  cardNumber: number;
  expiryDate: Date;
  cvv: number;
  pin: number;
  amount: number;
  flag: boolean;
  transaction: UserTransaction;
  card: Card;
  month: number[] = [];
  year: number[] = [];
  state = "active";
  submitted = false;
  page: number;
  pagination: any;
  banks: Bank[];

  /**
   * form group for load money usind saved card 
   * 
   * Validation of amount          - Amount will allow positive and negative numbers (Custom validator {@link AmountValidator})
   *                                 It should be rounded off to two decimal places
   *               pin             - pin should contain only 4 digits
   *               cvv             - cvv should contain only 3 digits
   *               validThru       - expiry date is required and this loaded by it self
   *               debitCardNumber - debit Card Number is required and this loaded by it self
   *                
   */
  form = this.fb.group({
    userId: [""],
    debitCardNumber: [""],
    cardHolderName: [""],
    validThru: [""],
    cvv: ["", [Validators.required, Validators.pattern("[0-9]{3}")]],
    pin: ["", [Validators.required, Validators.pattern("[0-9]{4}")]],
    amount: ["", [Validators.required, Validators.pattern("[-]?[0-9]+[.]?[0-9]{0,2}"), AmountValidator.min]]
  });

  /**
   * form group for load money usind net banking
   * 
   * Validation of amount          - Amount will allow positive and negative numbers (Custom validator {@link AmountValidator})
   *                                 It should be rounded off to two decimal places
   */
  netForm = this.fb.group({
    amount: ["", [Validators.required, Validators.pattern("[-]?[0-9]+[.]?[0-9]{0,2}"), AmountValidator.min]]
  });


  /**
   * form group for load money using new card 
   * 
   * Validation of amount          - Amount will allow positive and negative numbers (Custom validator {@link AmountValidator})
   *                                 It should be rounded off to two decimal places
   *               pin             - pin should contain only 4 digits
   *               cvv             - cvv should contain only 3 digits
   *               cardHolderName  - Name will allow only alphabets and spaces
   *               debitCardNumber - debitCardNumber should contain only 16 digits
   *               validMonth      - which should be selected from the dropdown having values 1 to 12
   *               validYear       - which should be selected from the dropdown having values current year to current year+20
   *               validThru       - (expiryDate) setted in the code once user submite using validMonth and validYear
   */
  newCardForm = this.fb.group({
    bank: ["", Validators.required],
    cardHolderName: ["", [Validators.required, Validators.pattern("[A-Za-z]+([ ][A-Za-z]+)*")]],
    debitCardNumber: ["", [Validators.required, Validators.pattern("[0-9]{16}")]],
    validMonth: ["", Validators.required],
    validYear: ["", Validators.required],
    validThru: [""],
    cvv: ["", [Validators.required, Validators.pattern("[0-9]{3}")]],
    pin: ["", [Validators.required, Validators.pattern("[0-9]{4}")]],
    amount: ["", [Validators.required, Validators.pattern("[-]?[0-9]+[.]?[0-9]{0,2}"), AmountValidator.min]]
  });

  /** constructor will be executed on creation of object creation 
    * 
    * the objects specified as parameters will be injected while execution 
    * and these are used as instance variables 
    */
  constructor(public fb: FormBuilder, private uriService: UriService, private logger: LoggerService,
    private loadMoneyService: LoadMoneyService, private profileService: ProfileService,
    private translateService: TranslateService) { }

  /**
   * Life cycle method on init (overided from OnInit)
   */
  ngOnInit() {
    this.page = 1;
    let user = JSON.parse(sessionStorage.getItem("user"));
    this.cards = user.cards;


    for (let i = 1; i <= 12; i++) {
      this.month.push(i);
    }
    for (let i = (new Date).getFullYear(); i < (new Date).getFullYear() + 20; i++) {
      this.year.push(i);
    }

    this.loadMoneyService.getAllBanks().subscribe(responseData => {
      this.banks = responseData;
      this.logger.info("Successfully fetched the banks");
    }, error => {
      this.logger.error("Fetching the banks failed");
      this.translateService.get("ERROR_MESSAGES.BANKS_FETCH_FAILED").subscribe(value => this.message = value);
    })

  }

  /**
   * This method is used for changing the type of payment 
   * i.e debit card/Net banking for laoding money
   */
  changeType(type: string) {
    this.submitted = false;
    this.paymentType = type;
    this.removeFlag = false;
    this.message = null;
    this.successMessage = null;
  }

  /**
   * This method is called when the next card 
   * or previous card pagination clicked
   */
  pageChange() {
    this.selectedCard = null;
    this.successMessage = null;
    this.message = null;
  }

  /**
   * This method is used for adding a new card for loading money
   */
  addNewCardClick() {
    this.submitted = false;
    this.addCardFlag = true;
    this.removeFlag = false;
    this.message = null;
    this.successMessage = null;
  }

  /**
   * This method is used for using a saved card for loading money
   */
  savedCardClick(cardNumber: any) {
    this.submitted = false;
    this.selectedCard = cardNumber;
    this.addCardFlag = false;
    this.message = null;
    this.successMessage = null;
    this.removeFlag = false;
    this.form.reset();
    this.newCardForm.reset();
    this.newCardForm.get('validMonth').setValue('');
    this.newCardForm.get('validYear').setValue('');
    this.newCardForm.get('bank').setValue('');
  }

  /**
   * This method calls the cardPayment method of 
   * LoadMoneyService and then calls the loadMoneyDebitCard method of 
   * LoadMoneyService which returns a UserTransaction.
   * It displays appropriate error message in case of any error
   */
  addMoney(card: Card) {
    this.form.controls["debitCardNumber"].setValue(card.cardNumber);
    this.form.get('cardHolderName').setValue('SAVED_CARD_PAYMENT');
    this.submitted = true;
    this.successMessage = null;
    this.message = null;
    this.removeFlag = false;
    this.amount = this.form.controls["amount"].value;
    this.loadMoneyService.cardVerification(this.form.value).subscribe((responseData: any) => {
      this.loadMoneyService.cardPayment(this.amount, this.form.value).subscribe((responseData: any) => {
        this.form.controls['userId'].setValue(parseInt(sessionStorage.getItem("userId"), 10));

        this.loadMoneyService.loadMoneyDebitCard(this.amount, this.form.value).subscribe((responseData1: any) => {
          this.profileService.addMoney(this.amount);
          this.translateService.get("SUCCESS_MESSAGES.ADD_MONEY_SUCCESS").subscribe(value => this.successMessage = value);
          this.form.reset();
          this.submitted = false;
          this.logger.info(this.successMessage);
        }, error => {
          if (error.error.message != null) {
            this.message = error.error.message;
          } else {
            this.translateService.get("ERROR_MESSAGES.SERVER_DOWN").subscribe(value => this.message = value)
              ;
          }
          this.submitted = false;
          this.logger.error(this.message, error);
        });
      },
        error => {
          error.error = JSON.parse(error.error);
          if (error.error.message != null) {
            this.message = error.error.message;
          } else {
            this.translateService.get("ERROR_MESSAGES.SERVER_DOWN").subscribe(value => this.message = value)
              ;
          }
          this.submitted = false;
          this.logger.error(this.message, error);
        });
    },
      error => {
        if (error.error.message != null) {
          this.message = error.error.message;
        } else {
          this.translateService.get("ERROR_MESSAGES.SERVER_DOWN").subscribe(value => this.message = value)
            ;
        }
        this.submitted = false;
        this.logger.error(this.message, error);
      });
  }

  /**
   * This method calls the deleteCard method of
   * LoadMoneyService which returns a card model.
   * It then removes this card from the user's saved cards list.
   * In case of any error it displays appropriate error message
   */
  removeCard(cardId: number) {
    this.successMessage = null;
    this.message = null;
    let confirmMessage = null;
    this.removeFlag = true;
    this.loadMoneyService.deleteCard(cardId.toString()).subscribe((responseData: any) => {
      let card: Card = responseData;
      this.card = card;
      let user: User = JSON.parse(sessionStorage.getItem("user"));
      let newCardList: Card[] = this.cards.filter(card1 => card1.cardId != cardId);
      user.cards = newCardList;
      this.cards = newCardList;
      this.logger.info("Card removed successfully");
      sessionStorage.setItem("user", JSON.stringify(user));
      this.translateService.get("SUCCESS_MESSAGES.CARD_DELETE_SUCCESS").subscribe(value => this.successMessage = value);
      console.log(((!this.addCardFlag && false) || this.removeFlag) && (this.successMessage != null || this.message != null));
    },
      error => {
        if (error.error.message != null) {
          this.message = error.error.message;
        } else {
          this.translateService.get("ERROR_MESSAGES.SERVER_DOWN").subscribe(value => this.message = value)
            ;
        }
        this.logger.error(this.message, error);
      });
  }

  /**
   * This method redirects to netBankingLoginView of EDUBank
   */
  netBanking() {
    this.submitted = true;
    this.successMessage = null;
    this.message = null;
    this.removeFlag = false;
    let edubankUrl = this.uriService.buildEduBankUri();
    let amount = this.netForm.controls["amount"].value;
    window.setTimeout('window.location="' + edubankUrl + '/netBankingLoginView/' + amount +
      '?protocol=' + location.protocol +
      '&host=' + location.hostname +
      '&port=' + location.port +
      '&requestPage=netBankingSuccess"; ', 100);
    this.logger.info("Redirecting to net banking gateway");
  }

  /**
   * This method calls the cardVerification method of LoadMoneyService.
   * Then it calls the addCard method of LoadMoneyService.
   * It then calls the cardPayment method of 
   * LoadMoneyService and then calls the loadMoneyDebitCard method of 
   * LoadMoneyService which returns a UserTransaction.
   * The new card is then added to the local user's saved cards list,
   * which will internally add the card in the display
   * It displays appropriate error message in case of any error
   */
  loadMoneyByNewCard() {
    this.submitted = true;
    this.successMessage = null;
    this.message = null;
    this.removeFlag = false;
    let monthSelected = parseInt(this.newCardForm.controls["validMonth"].value, 10);
    let yearSelected = parseInt(this.newCardForm.controls["validYear"].value, 10);
    this.newCardForm.controls["validThru"].setValue(new Date("" + yearSelected + "-" + monthSelected + "-" + 15));

    this.loadMoneyService.cardVerification(this.newCardForm.value).subscribe((responseData: any) => {
      let user: User = JSON.parse(sessionStorage.getItem("user"));
      let card: Card = new Card();
      let bank: Bank = new Bank();
      bank.bankId = this.newCardForm.controls["bank"].value;
      bank = this.banks.filter(value => bank.bankId == value.bankId)[0];
      console.log(bank);
      card.bank = bank;
      card.cardNumber = this.newCardForm.controls["debitCardNumber"].value;
      card.expiryDate = this.newCardForm.controls["validThru"].value;
      card.cvv = this.newCardForm.controls["cvv"].value;
      this.loadMoneyService.addCard(user.userId, card).subscribe((responseData1: any) => {
        let amount = this.newCardForm.controls["amount"].value;
        card.pin = this.newCardForm.controls["pin"].value;
        card.cardId = responseData1.cardId;

        this.loadMoneyService.cardPayment(amount, this.newCardForm.value).subscribe((responseData2: any) => {
          let userToEWallet = new User();
          userToEWallet.userId = user.userId;

          this.loadMoneyService.loadMoneyDebitCard(amount, userToEWallet).subscribe((responseData3: any) => {
            this.transaction = responseData3;
            let strUser: string = sessionStorage.getItem("user");
            this.user = JSON.parse(strUser);
            if (this.user.cards == null) {
              this.user.cards = [];
            } if (this.cards == null) {
              this.cards = [];
            }
            this.cards.push(card);
            this.user.cards = this.cards;
            this.profileService.addMoney(amount);
            this.user.balance = this.profileService.getBalance();
            sessionStorage.setItem("user", JSON.stringify(this.user));
            this.newCardForm.reset();
            this.translateService.get("SUCCESS_MESSAGES.LOAD_CARD_SUCCESS").subscribe(value => this.successMessage = value);
            this.submitted = false;
            this.logger.info(this.successMessage);

          }, error => {
            if (error.error.message != null) {
              this.message = error.error.message;
            } else {
              this.translateService.get("ERROR_MESSAGES.SERVER_DOWN").subscribe(value => this.message = value)
                ;
            }
            this.submitted = false;
            this.logger.error(this.message, error);
          });
        }, error => {
          error.error = JSON.parse(error.error);
          if (error.error.message != null) {
            this.message = error.error.message;
          } else {
            this.translateService.get("ERROR_MESSAGES.SERVER_DOWN").subscribe(value => this.message = value)
              ;
          }
          this.submitted = false;
          this.logger.error(this.message, error);
        });
      }, error => {
        if (error.error.message != null) {
          this.message = error.error.message;
        } else {
          this.translateService.get("ERROR_MESSAGES.SERVER_DOWN").subscribe(value => this.message = value)
            ;
        }
        this.submitted = false;
        this.logger.error(this.message, error);
      });
    }, error => {
      error.error = JSON.parse(error.error);
      if (error.error.message != null) {
        this.message = error.error.message;
      } else {
        this.translateService.get("ERROR_MESSAGES.SERVER_DOWN").subscribe(value => this.message = value)
          ;
      }
      this.submitted = false;
      this.logger.error(this.message, error);
    });

  }
}