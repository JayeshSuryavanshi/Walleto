import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { WalletComponent } from './wallet.component';
import { LoadMoneyComponent } from './load-money/load-money.component';
import { PointsComponent } from './points/points.component';
import { RoutingGuard } from '../shared/routing.guard';
import { ChangePasswordComponent } from './change-password/change-password.component';
import { ErrorComponent } from './error/error.component';
import { NetBankingSuccessComponent } from './load-money/net-banking-success/net-banking-success.component';
import { BillpaymentComponent } from '../billpayment/billpayment.component';
import { TransactionHistoryComponent } from './transaction-history/transaction-history.component';

import { TransferToBankComponent } from './transfer-to-bank-component/transfer-to-bank-component.component';
import { WalletToWalletTransferComponent } from './wallet-to-wallet-transfer/wallet-to-wallet-transfer.component';
import { ExpenseTrackingComponent } from './expense-tracking/expense-tracking.component';

/**
 * This class is used to map the url to the components
 * i.e when the specified url is encountered which component has to be loaded
 * to the appComponent
 */
const routes: Routes = [

    /** Once the login is successful the page will be redirected to home component
     * the home component can load different component inside it which is on request.
     * The components which gets loaded inside home component are called as children components.
     *
     * canActivate: loads the specified service class i.e RoutingGaurdService.
     *              Inside RoutingGaurdService we are checking weather the user has logged in or not.
     *              if yes Home component will be activated and loaded
     *              or else the user will be routed to error page
     */
    {
        path: 'home', component: WalletComponent, canActivate: [RoutingGuard], children: [
            { path: 'addMoney', component: LoadMoneyComponent },
            { path: 'towallet', component: WalletToWalletTransferComponent },
            { path: 'redeem', component: PointsComponent },
            { path: 'banktransfer', component: TransferToBankComponent },
            { path: 'redeem', component: PointsComponent },
            { path: 'billpayment', component: BillpaymentComponent },
            { path: 'expenseTracking', component: ExpenseTrackingComponent },
            { path: 'viewtxn', component: TransactionHistoryComponent }
        ]
    },

    /** If any application encounters changePassword in url it will load ChangePasswordComponent
     * which is the option given for the logged in user for changing his password
     *
     * canActivate: loads the specified service class i.e RoutingGaurdService.
     *              Inside this class we will check weather the user has logged in if so
     *              ChangePasswordComponent will be activated and loaded.
     *              or else in the RoutingGaurdService we will route to error page
    */
    { path: 'changePassword', component: ChangePasswordComponent, canActivate: [RoutingGuard] },

    /** If any application encounters error in url it will load ErrorComponent
     */
    { path: 'error', component: ErrorComponent },

    /** If any application encounters netBankingSuccess/:amt/:msg in url it will load ResetPasswordComponent
     * here :amt and :msg sre the parameters or values sent in the url
     */
    { path: 'netBankingSuccess/:amt/:msg', component: NetBankingSuccessComponent, canActivate: [RoutingGuard] }
];


@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class WalletRoutingModule { }
