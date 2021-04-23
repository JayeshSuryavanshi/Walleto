import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { NetBankingSuccessComponent } from './net-banking-success.component';

import { UriService } from "../../../shared/uri.service";
import { LoggerService } from "../../../shared/logger.service";
import { RouterTestingModule } from "@angular/router/testing";
import { TranslateModule, TranslateLoader } from "@ngx-translate/core";
import { createTranslateLoader } from "../../../app.module";
import { ProfileService } from "../../../shared/profile.service";
import { ActivatedRoute } from "@angular/router";
import { Injectable } from "@angular/core";

import { User } from "../../../shared/model/user";
import { BehaviorSubject } from 'rxjs';
import { HttpClientModule, HttpClient } from '@angular/common/http';

@Injectable()
export class ActivatedRouteStub
{
    private subject = new BehaviorSubject(this.testParams);
    params = this.subject.asObservable();

    private _testParams: {};
    get testParams() { return this._testParams; }
    set testParams(params: {}) {
        this._testParams = params;
        this.subject.next(params);
    }
}

describe('NetBankingSuccessComponent', () => {
  let component: NetBankingSuccessComponent;
  let fixture: ComponentFixture<NetBankingSuccessComponent>;
  let mockActivatedRoute;

  beforeEach(async(() => {
    mockActivatedRoute = new ActivatedRouteStub();
    TestBed.configureTestingModule({
      imports: [
        RouterTestingModule,
        HttpClientModule,
        TranslateModule.forRoot({
          loader: {
            provide: TranslateLoader,
            useFactory: (createTranslateLoader),
            deps: [HttpClient]
          }
        })
      ],
      declarations: [ NetBankingSuccessComponent ],
      providers: [UriService, LoggerService, ProfileService, {provide: ActivatedRoute, useValue: mockActivatedRoute}]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(NetBankingSuccessComponent);
    component = fixture.componentInstance;
    mockActivatedRoute.testParams = {msg: '12345678', amt: '12_34'};
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
