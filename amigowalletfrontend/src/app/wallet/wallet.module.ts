import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { NgxPaginationModule } from 'ngx-pagination';

import { WalletRoutingModule } from './wallet-routing.module';
import { ChangePasswordComponent } from './change-password/change-password.component';
import { LoadMoneyComponent } from './load-money/load-money.component';
import { PointsComponent } from './points/points.component';
import { ProfileComponent } from './profile/profile.component';
import { ErrorComponent } from './error/error.component';
import { ProfileService } from '../shared/profile.service';
import { TranslateModule, TranslateLoader } from '@ngx-translate/core';
import { TranslateHttpLoader } from '@ngx-translate/http-loader';
import { NetBankingSuccessComponent } from './load-money/net-banking-success/net-banking-success.component';
import { WalletComponent } from './wallet.component';
import { HttpClient, HttpClientModule } from '@angular/common/http';

import { TransactionHistoryComponent } from './transaction-history/transaction-history.component';
import { TransferToBankComponent } from './transfer-to-bank-component/transfer-to-bank-component.component';
import { WalletToWalletTransferComponent } from './wallet-to-wallet-transfer/wallet-to-wallet-transfer.component';
import { SortPipe } from './transaction-history/sort.pipe';
import { FilterPipe } from './transaction-history/filter.pipe';
import { ExpenseTrackingComponent } from './expense-tracking/expense-tracking.component';

export function createTranslateLoader(http: HttpClient) {
  return new TranslateHttpLoader(http, 'assets/i18n/', '.json');
}

@NgModule({
  imports: [
    CommonModule,
    WalletRoutingModule,
    FormsModule,
    ReactiveFormsModule,
    HttpClientModule,
    NgxPaginationModule,
    TranslateModule.forRoot({
      loader: {
        provide: TranslateLoader,
        useFactory: (createTranslateLoader),
        deps: [HttpClient]
      }
    })
  ],
  declarations: [
    WalletComponent,
    ChangePasswordComponent,
    LoadMoneyComponent,
    PointsComponent,
    ProfileComponent,
    ErrorComponent,
    NetBankingSuccessComponent,
    
    TransactionHistoryComponent,
    TransferToBankComponent,
    WalletToWalletTransferComponent,
    SortPipe,
    FilterPipe,
    ExpenseTrackingComponent
  ],
  providers: [ProfileService]
})
export class WalletModule { }
