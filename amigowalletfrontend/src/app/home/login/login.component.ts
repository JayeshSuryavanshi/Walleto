import { Component, OnInit } from '@angular/core';
import { AbstractControl, FormBuilder, Validators } from '@angular/forms';
import { Router } from "@angular/router";
import { TranslateService } from '@ngx-translate/core';

import { Card } from '../../shared/model/card';
import { User } from '../../shared/model/user';

import { LoggerService } from '../../shared/logger.service';
import { PasswordValidator } from '../../shared/password.validator';

import { LoginService } from './login.service';
import { UserTransaction } from "../../shared/model/user-transaction";

@Component({
  selector: 'aw-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css'],
  providers: [LoginService]
})
export class LoginComponent implements OnInit {

  /** class instance variales */
  user: User;
  cards: Card[];
  userTransactions: UserTransaction[];
  message: string;
  error: any;
  submitted = false;


  /**
   * Validation of password - Password should be within 8-20 characters with at least 
   *                          one uppercase, one lowercase, one digit and 
	 *                          special character in (!,#,$,%,^,&,*,(,))
   *               emailId  - Email id should follow a valid format
   */
  form = this.fb.group({
    emailId: ["", [Validators.required, Validators.pattern("[^@]+[@][^@]+[.][^@]+")]],
    password: ["", Validators.required]
  });

  /** constructor will be executed on creation of object creation 
   * 
   * the objects specified as parameters will be injected while execution 
   * and these are used as instance variables 
   */
  constructor(public fb: FormBuilder, private loginService: LoginService, private router: Router,
    private logger: LoggerService, private translateService: TranslateService) { }


  /**
   * Life cycle method on init (overided from OnInit)
   */
  ngOnInit() {
  }


  /**
   * This method calls doLogin method of LoginService
   * which returns a user model populated with appropriate
   * message.
   * userId and user are stored in sessionStorage
   * In case of success it navigates to home page
   * In case of failure it displays an error message
   */
  authenticate() {
    this.submitted = true;
    this.message = null;
    let passwordControl: AbstractControl = this.form.get("password");

    if (!PasswordValidator.minLength(passwordControl) &&
      !PasswordValidator.requiredALowerCase(passwordControl) &&
      !PasswordValidator.requiredANumber(passwordControl) &&
      !PasswordValidator.requiredASpecialChar(passwordControl) &&
      !PasswordValidator.requiredAUpperCase(passwordControl)) {

      this.loginService.doLogin(this.form.value).subscribe(
        (responseData: any) => {
          let user: User = responseData;
          this.user = user;
          sessionStorage.setItem("userId", this.user.userId.toString());
          sessionStorage.setItem("user", JSON.stringify(this.user));
          this.router.navigate(['/home']);
          this.logger.info("Successfull login");
        },
        error => {
          if (error.error != null) {
            this.message = error.error.message;
          } else {
            this.translateService.get("ERROR_MESSAGES.SERVER_DOWN").subscribe(value => this.message = value)
              ;
          }
          this.logger.error(this.message, error);
          this.submitted = false;
        });

    } else {
      this.logger.warn("Password format error");
      this.translateService.get("ERROR_MESSAGES.INVALID_CREDENTIALS").subscribe(value => this.message = value);
      this.submitted = false;
    }
  }

}
