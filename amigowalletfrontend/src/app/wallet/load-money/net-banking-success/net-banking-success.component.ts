import { Location } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Params, Router } from "@angular/router";
import { TranslateService } from '@ngx-translate/core';

import { User } from '../../../shared/model/user';
import { UserTransaction } from '../../../shared/model/user-transaction';

import { LoggerService } from '../../../shared/logger.service';
import { ProfileService } from '../../../shared/profile.service';

import { LoadMoneyService } from '../load-money.service';

// This component is loaded after the successful netbanking

/** Annotation which specifes this as a component 
 * 
 * moduleId: This specified for specifing that the path used in the component are relative to this component
 * templateUrl: This is the relative path for the html related to this component
 * styleUrls: This is the relative path for the css related to this component
 * providers: specifies the service class which communicates with the Serverside application
 *            specifing here makes it available only in this class
*/
@Component({
  templateUrl: './net-banking-success.component.html',
  styleUrls: ['./net-banking-success.component.css'],
  providers: [LoadMoneyService]
})
export class NetBankingSuccessComponent implements OnInit {

  /** class instance variales */
  successMessage: string;
  errorMessage: string;
  loading = true;
  redirectMessage: string;

  /** constructor will be executed on creation of object creation 
   * 
   * the objects specified as parameters will be injected while execution 
   * and these are used as instance variables 
   */
  constructor(private activatedRoute: ActivatedRoute, private loadMoneyService: LoadMoneyService,
    private router: Router, private profileService: ProfileService, private location: Location,
    private logger: LoggerService, private translateService: TranslateService) {
  }

  /**
   * This method checks the message and if the transaction was successfull
   * ,it calls the netBanking method of loadMoneyService which 
   * returns a UserTransaction
   * This UserTransaction is pushed into the user's transactions list
   * In case of success it displays a success message
   * In case of failure it displays appropriate error message
   */
  ngOnInit() {
    this.activatedRoute.params.subscribe((params: Params) => {
      let message: string = params['msg'];
      let amount: string = params['amt'];

      amount = (amount) ? amount.replace("_", ".") : '';
      message = (message.match("^[0-9]+$") != null) ? message : '';
      this.location.replaceState("netBankingSuccess");

      if (message.match("^[0-9]+$") != null) {
        let user: User = JSON.parse(sessionStorage.getItem("user"));
        let obj: User = new User();
        obj.userId = user.userId;
        obj.balance = parseFloat(amount);
        this.loadMoneyService.netBanking(obj).subscribe((responseData: any) => {
          let transaction: UserTransaction = responseData;
          user.balance += parseFloat(amount);
          this.profileService.addMoney(parseFloat(amount));
          sessionStorage.setItem("user", JSON.stringify(user));
          this.translateService.get("SUCCESS_MESSAGES.NET_BANKING_SUCCESS").subscribe(value => this.successMessage = value);
          this.successMessage += transaction.userTransactionId;
          this.translateService.get("OTHER.REDIRECT_NET_BANKING").subscribe(value => this.redirectMessage = value);
          this.loading = false;
          this.logger.info(this.successMessage);
          setTimeout(() => {
            this.router.navigate(["/home"]);
          }, 1500);
        }, error => {
          this.translateService.get("ERROR_MESSAGES.SERVER_DOWN").subscribe(value => this.errorMessage = value);
          this.logger.error(this.errorMessage, error);
          this.loading = false;
        });
      } else {
        this.translateService.get("ERROR_MESSAGES.TRANSACTION_CANCELED").subscribe(value => this.errorMessage = value);
        this.logger.error(this.errorMessage);
        this.loading = false;
      }
    });

  }
}