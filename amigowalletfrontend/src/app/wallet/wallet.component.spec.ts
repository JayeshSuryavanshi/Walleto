import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { WalletComponent } from './wallet.component';
import { ReactiveFormsModule } from "@angular/forms";
import { TranslateModule, TranslateLoader } from "@ngx-translate/core";
import { NgxPaginationModule } from "ngx-pagination/dist/ngx-pagination";
import { createTranslateLoader } from "./wallet.module";
import { ChangePasswordComponent } from "./change-password/change-password.component";
import { LoadMoneyComponent } from "./load-money/load-money.component";
import { PointsComponent } from "./points/points.component";
import { ProfileComponent } from "./profile/profile.component";
import { ErrorComponent } from "./error/error.component";
import { NetBankingSuccessComponent } from "./load-money/net-banking-success/net-banking-success.component";
import { ProfileService } from "../shared/profile.service";
import { RouterTestingModule } from "@angular/router/testing";
import { HttpClientModule, HttpClient } from '@angular/common/http';
import { UriService } from '../shared/uri.service';

describe('WalletComponent', () => {
  let component: WalletComponent;
  let fixture: ComponentFixture<WalletComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      imports: [
        RouterTestingModule,
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
        NetBankingSuccessComponent
      ],
      providers: [ProfileService, UriService]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(WalletComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should be created', () => {
    expect(component).toBeTruthy();
  });
});
