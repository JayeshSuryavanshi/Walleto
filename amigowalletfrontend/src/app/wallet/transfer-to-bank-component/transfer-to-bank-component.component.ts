import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ProfileService } from 'src/app/shared/profile.service';
import { TransferToBankService } from './transfer-to-bank.service';
import { TranslateService } from '@ngx-translate/core';
import { User } from 'src/app/shared/model/user';

@Component({
  selector: 'app-transfer-to-bank-component',
  templateUrl: './transfer-to-bank-component.component.html',
  styleUrls: ['./transfer-to-bank-component.component.css']
})
export class TransferToBankComponent implements OnInit {

  bankAccountForm: FormGroup;
  errorMessage: string;
  successMessage: string;
  transactionId: string;

  constructor(
    private formBuilder: FormBuilder,
    private profileService: ProfileService,
    private transferToBankService: TransferToBankService,
    private translateService: TranslateService,
  ) { }

  ngOnInit() {
    this.bankAccountForm = this.formBuilder.group({
      ifsc: ['',
        [
          Validators.required,
          Validators.pattern(/^[A-Za-z]{4}[0-9]{7}$/)
        ]
      ],
      accountNumber: ['',
        [
          Validators.required,
          Validators.pattern(/^[0-9]{15}$/)
        ]
      ],
      accountHolderName: ['',
        [
          Validators.required,
          Validators.pattern(/^[A-Za-z ]+$/)
        ]
      ],
      amount: ['',
        [
          Validators.required,
          Validators.min(1),
          Validators.pattern(/^[0-9]+(.[0-9]{2})?$/)
        ]
      ]
    });
  }

  userHasSufficientBalance(): boolean {
    if (this.profileService.getBalance()
      < this.bankAccountForm.controls.amount.value) {
      return false;
    }
    return true;
  }

  verifyAccountDetails(): boolean {

    let accountVerified: boolean;

    this.transferToBankService.accountVerification(
      this.bankAccountForm.value
    ).subscribe(
      (accountFound) => { accountVerified = true; },
      (error) => { accountVerified = false; },
    );

    return accountVerified;
  }

  onSubmit() {

    this.errorMessage = null;
    this.successMessage = null;
    this.transactionId = null;

    if (!this.userHasSufficientBalance()) {
      this.errorMessage = 'ERROR_MESSAGES.TRANSFER_BANK_LOW_BALANCE';
      return;
    }

    const amount = this.bankAccountForm.controls.amount.value;

    const accountDetails = {
      ifsc : this.bankAccountForm.controls.ifsc.value,
      accountNumber : this.bankAccountForm.controls.accountNumber.value,
      accountHolderName : this.bankAccountForm.controls.accountHolderName.value
    };


    this.transferToBankService.accountVerification(
      accountDetails
    ).subscribe(

      // account was verified !
      (resp) => {

        this.transferToBankService.creditMoney(
          amount,
          accountDetails,
        ).subscribe(

          // money was credited!
          (resp) => {

            this.profileService.subMoney(amount);

            // // update the amount value in the session storage
            // let userBalance = this.profileService.getBalance();
            // userBalance -= amount;
            // this.profileService.setBalance(userBalance);

            const user: User = JSON.parse(sessionStorage.getItem('user'));
            this.transferToBankService.sendToBank(user, amount).subscribe(

              // amigo wallet sucessfully transferred the amount to wallet
              (resp) => {
                this.successMessage = 'SUCCESS_MESSAGES.TRANSFER_TO_BANK_SUCCESS';
                this.transactionId = JSON.parse(resp).userTransactionId;
                // console.log(resp);
                // console.log(this.successMessage);
                // console.log(this.transactionId);
                // console.log(resp.message);
                this.bankAccountForm.reset();
              },
              (errorResponse) => {
                let error: any;
                try {
                  error = JSON.parse(errorResponse.error).message || 'ERROR_MESSAGES.SERVER_DOWN';
                } catch (e) {
                  error = 'ERROR_MESSAGES.SERVER_DOWN'
                }
                this.errorMessage = error;              },
            );

          },

          // money was not credited there was an error
          (errorResponse) => {
            let error: any;
            try {
              error = JSON.parse(errorResponse.error).message || 'ERROR_MESSAGES.SERVER_DOWN';
            } catch (e) {
              error = 'ERROR_MESSAGES.SERVER_DOWN'
            }
            this.errorMessage = error;          },

        );

      },

      // account was not verified
      (errorResponse) => {
        // console.log(errorResponse);
        let error: any;
        try {
          error = JSON.parse(errorResponse.error).message || 'ERROR_MESSAGES.SERVER_DOWN';
        } catch (e) {
          error = 'ERROR_MESSAGES.SERVER_DOWN'
        }
        this.errorMessage = error;
      },

      // finally
      () => {

      }

    );

  }
}
