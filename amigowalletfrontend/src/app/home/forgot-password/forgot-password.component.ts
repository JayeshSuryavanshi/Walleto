import { Component, OnInit } from '@angular/core';
import { FormBuilder, Validators } from '@angular/forms';

import { LoggerService } from '../../shared/logger.service';
import { TranslateService } from '@ngx-translate/core';
import { ForgotPasswordService } from './forgot-password.service';
import { User } from '../../shared/model/user';
import { PasswordValidator } from '../../shared/password.validator';
import { Router } from '@angular/router';

// This component gives the takes the email and sends the reset password link

/** Annotation which specifes this as a component 
 * 
 * templateUrl: This is the relative path for the html related to this component
 * styleUrls: This is the relative path for the css related to this component
 * providers: specifies the service class which communicates with the Serverside application
 *            specifing here makes it available only in this class
*/
@Component({
  templateUrl: './forgot-password.component.html',
  styleUrls: ['./forgot-password.component.css'],
  providers: [ForgotPasswordService]
})
export class ForgotPasswordComponent implements OnInit {

  /** class instance variales */
  user: User;
  message: string;
  error: any;
  emailId: string;
  redirectMessage: any;
  successMessage: string;
  submitted = false;
  step: number;

  /**
   * form group for getting emailId
   * 
   * Validation of emailId      - Email id should follow a valid format
   */
  form = this.fb.group({
    emailId: ["", [Validators.required, Validators.pattern("[^@]+[@][^@]+[.][^@]+")]],
  });


  /**
   * form group for getting the answer for the security question
   * 
   * Validation of emailId      - Email id should follow a valid format
   */
  securityForm = this.fb.group({
    securityAnswer: ["", [Validators.required]],
  });

  resetForm = this.fb.group({
    newPassword: [
      "", [Validators.required, PasswordValidator.minLength, PasswordValidator.maxLength,
      PasswordValidator.requiredALowerCase, PasswordValidator.requiredANumber,
      PasswordValidator.requiredASpecialChar, PasswordValidator.requiredAUpperCase]],
    confirmNewPassword: [
      "", Validators.required]
  });


  /** constructor will be executed on creation of object creation 
     * 
     * the objects specified as parameters will be injected while execution 
     * and these are used as instance variables 
     */
  constructor(public fb: FormBuilder, private forgotPasswordService: ForgotPasswordService,
    private logger: LoggerService, private translateService: TranslateService, private router: Router) { }

  /**
   * Life cycle method on init (overided from OnInit)
   */
  ngOnInit() { this.step = 0; }


  /**
   * This method validates emailId
   * If valid it calls forgotPassword method of ForgotPasswordService
   *    which returns a message as a string                     
   * If not valid it displays an appropriate error message
   */
  authenticate() {
    this.submitted = true;
    this.successMessage = null;
    this.message = null;
    this.forgotPasswordService.forgotPassword(this.form.controls["emailId"].value).subscribe(
      (responseData: User) => {
        this.user = responseData;
        this.submitted = false;
        this.step = 1;
      },
      error => {
        console.log(error);
        if (error.error.message == null) {
          this.translateService.get("ERROR_MESSAGES.SERVER_DOWN").subscribe(value => this.message = value);
        } else {
          this.message = error.error.message;
        }
        this.logger.error(this.message, error);
        this.submitted = false;
      });
  }

  checkAnswer() {
    this.submitted = true;
    this.successMessage = null;
    this.message = null;
    this.user.securityAnswer = this.securityForm.controls["securityAnswer"].value;
    this.forgotPasswordService.checkAnswer(this.user).subscribe(
      (responseData: string) => {
        this.successMessage = responseData;
        this.submitted = false;
        this.step = 2;
      },
      error => {
        if (error.error.message == null) {
          this.translateService.get("ERROR_MESSAGES.SERVER_DOWN").subscribe(value => this.message = value);
        } else {
          this.message = error.error.message;
        }
        this.logger.error(this.message, error);
        this.submitted = false;
      });
  }

  /**
 * If valid it calls resetPasswordPost method of ResetPasswordService
 *    which returns a message as a string and redirects to the login page                      
 * If not valid it displays an appropriate error message
 */
  resetPasswordSubmit() {
    this.submitted = true;
    this.successMessage = null;
    this.message = null;
    this.user.password = this.resetForm.controls['newPassword'].value;
    this.forgotPasswordService.resetPasswordPost(this.user).subscribe(
      (responseData: any) => {
        let msg: string = responseData;
        this.successMessage = msg;
        this.translateService.get("OTHER.REDIRECT2").subscribe(value => this.redirectMessage = value);
        this.form.reset();
        this.logger.info(msg);
        setTimeout(() => {
          this.router.navigate(['login'])
        }, 3000);
      },
      error => {
        if (!error.error.message) {
          this.translateService.get("ERROR_MESSAGES.SERVER_DOWN").subscribe(value => this.message = value);
        } else {
          this.message = error.error.message;
        }
        this.logger.error(this.message);
        this.submitted = false;
      });
  }


}