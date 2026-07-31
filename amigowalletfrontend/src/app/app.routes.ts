import { Routes } from '@angular/router';

import { authGuard } from './shared/auth.guard';
import { HomeComponent } from './home/home.component';
import { SecurityQuestionComponent } from './home/security-question/security-question.component';
import { ForgotPasswordComponent } from './home/forgot-password/forgot-password.component';
import { WalletComponent } from './wallet/wallet.component';
import { LoadMoneyComponent } from './wallet/load-money/load-money.component';
import { WalletToWalletTransferComponent } from './wallet/wallet-to-wallet-transfer/wallet-to-wallet-transfer.component';
import { PointsComponent } from './wallet/points/points.component';
import { TransferToBankComponent } from './wallet/transfer-to-bank-component/transfer-to-bank-component.component';
import { BillpaymentComponent } from './billpayment/billpayment.component';
import { ExpenseTrackingComponent } from './wallet/expense-tracking/expense-tracking.component';
import { TransactionHistoryComponent } from './wallet/transaction-history/transaction-history.component';
import { ChangePasswordComponent } from './wallet/change-password/change-password.component';
import { ErrorComponent } from './wallet/error/error.component';

export const routes: Routes = [
  { path: '', redirectTo: 'login', pathMatch: 'full' },

  // Public (auth) screens
  { path: 'login', component: HomeComponent },
  { path: 'security', component: SecurityQuestionComponent },
  { path: 'forgotPassword', component: ForgotPasswordComponent },

  // Authenticated wallet dashboard + children (guarded by a valid JWT)
  {
    path: 'home',
    component: WalletComponent,
    canActivate: [authGuard],
    children: [
      { path: 'addMoney', component: LoadMoneyComponent },
      { path: 'towallet', component: WalletToWalletTransferComponent },
      { path: 'redeem', component: PointsComponent },
      { path: 'banktransfer', component: TransferToBankComponent },
      { path: 'billpayment', component: BillpaymentComponent },
      { path: 'expenseTracking', component: ExpenseTrackingComponent },
      { path: 'viewtxn', component: TransactionHistoryComponent },
    ],
  },
  { path: 'changePassword', component: ChangePasswordComponent, canActivate: [authGuard] },

  { path: 'error', component: ErrorComponent },
  { path: '**', redirectTo: 'login' },
];
