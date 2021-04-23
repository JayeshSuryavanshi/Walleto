import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { LoadMoneyComponent } from './load-money.component';
import { RouterTestingModule } from "@angular/router/testing";
import { ReactiveFormsModule } from "@angular/forms";
import { TranslateModule, TranslateLoader } from "@ngx-translate/core";
import { createTranslateLoader } from "../../app.module";
import { UriService } from "../../shared/uri.service";
import { LoggerService } from "../../shared/logger.service";
import { NgxPaginationModule } from "ngx-pagination/dist/ngx-pagination";
import { ProfileService } from "../../shared/profile.service";
import { User } from "../../shared/model/user";
import { BrowserAnimationsModule } from "@angular/platform-browser/animations";
import { HttpClientModule, HttpClient } from '@angular/common/http';

describe('LoadMoneyComponent', () => {
  let component: LoadMoneyComponent;
  let fixture: ComponentFixture<LoadMoneyComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      imports: [
        BrowserAnimationsModule,
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
      declarations: [LoadMoneyComponent],
      providers: [UriService, LoggerService, ProfileService]
    })
      .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(LoadMoneyComponent);
    component = fixture.componentInstance;
    let user: User = new User();
    user.balance = 0;
    user.cards = [];
    sessionStorage.setItem("user", JSON.stringify(user));
    fixture.detectChanges();
  });

  afterEach(() => {
    sessionStorage.removeItem("user");
  });

  it('should be created', () => {
    expect(component).toBeTruthy();
  });
});
