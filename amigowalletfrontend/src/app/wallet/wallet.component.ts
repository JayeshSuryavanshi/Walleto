import { Component, OnInit } from '@angular/core';
import { Router, ActivatedRoute } from '@angular/router';
import { TranslateService } from '@ngx-translate/core';
import { User } from '../shared/model/user';
import { FeatureButton } from './feature-link';
import { trigger, transition, animate, style, state } from '@angular/animations';

/** this component is loaded once the user login, it is layout type of component which
    holds/loads the component for different functionality */


/** Annotation which specifes this as a component
 *
 * moduleId: This specified for specifing that the path used in the component are relative to this component
 * templateUrl: This is the relative path for the html related to this component
 * styleUrls: This is the relative path for the css related to this component
*/
@Component({
  templateUrl: 'wallet.component.html',
  styleUrls: ['wallet.component.css'],
  animations: [
    trigger('expandCollapse', [
      state('void', style({
        height: "0px",
        overflow: "hidden"
      })),
      //element being added into DOM.
      transition(':enter', [
        animate(
          "500ms ease-in-out",
          style({
            height: "*",
            overflow: "hidden"
          })
        )
      ]),
      transition(":leave", [
        animate(
          "500ms ease-in-out",
          style({
            height: "0px",
            overflow: "hidden"
          })
        )
      ])
    ])
  ]
})
export class WalletComponent implements OnInit {

  /** class instance variales */
  user: User;
  selectedOption: number;
  backgroundWidth: string;
  backgroundHeight: string;
  featureButtons: FeatureButton[];

  /** constructor will be executed on creation of object creation
   *
   * the objects specified as parameters will be injected while execution
   * and these are used as instance variables
   */
  constructor(
    private _router: Router,
    private _route: ActivatedRoute,
    private translateService: TranslateService
  ) { }

  /**
   * Life cycle method on init (overided from OnInit)
   */
  ngOnInit() {
    this.featureButtons = [
      {
        routerLink: 'addMoney',
        logoURL: 'assets/resources/images/logo1.PNG',
        featureName: this.translateService.instant('HOME.ADD_MONEY_TO_WALLET'),
      },
      {
        routerLink: 'redeem',
        logoURL: 'assets/resources/images/logo6.PNG',
        featureName: this.translateService.instant('HOME.REDEEM_POINTS'),
      },
      {
        routerLink: 'towallet',
        logoURL: 'assets/resources/images/logo2.PNG',
        featureName: this.translateService.instant('HOME.WALLET_TRANSFER'),
      },
      {
        routerLink: 'billpayment',
        logoURL: 'assets/resources/images/logo3.PNG',
        featureName: this.translateService.instant('HOME.PAY_BILL'),
      },
      {
        routerLink: 'banktransfer',
        logoURL: 'assets/resources/images/logo4.PNG',
        featureName: this.translateService.instant('HOME.TRANSFER_TO_BANK'),
      },
      {
        routerLink: 'viewtxn',
        logoURL: 'assets/resources/images/logo5.PNG',
        featureName: this.translateService.instant('HOME.VIEW_TRANSACTIONS'),
      },
      {
        routerLink: 'expenseTracking',
        logoURL: 'assets/resources/images/logo7.PNG',
        featureName: this.translateService.instant('HOME.TRACK_EXPENSES'),
      },


    ];
    const strUser: string = sessionStorage.getItem('user');
    this.user = JSON.parse(strUser);
  }

}
