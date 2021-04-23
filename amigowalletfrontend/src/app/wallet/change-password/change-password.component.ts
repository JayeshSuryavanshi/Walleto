import { Component, OnInit } from '@angular/core';
import { AbstractControl, FormBuilder, Validators } from '@angular/forms';
import { Router } from '@angular/router';

import { TranslateService } from '@ngx-translate/core';
import { LoggerService } from '../../shared/logger.service';
import { PasswordValidator } from '../../shared/password.validator';

import { ChangePasswordService } from './change-password.service';

// This component provides the provotion for changing password (only for logged in user)

/** Annotation which specifes this as a component 
 * 
 * moduleId: This specified for specifing that the path used in the component are relative to this component
 * templateUrl: This is the relative path for the html related to this component
 * styleUrls: This is the relative path for the css related to this component
 * providers: specifies the service class which communicates with the Serverside application
 *            specifing here makes it available only in this class
*/
@Component({
  templateUrl: './change-password.component.html',
  styleUrls: ['./change-password.component.css'],
  providers: [ChangePasswordService]
})
export class ChangePasswordComponent implements OnInit {

  /** class instance variales */
  message: string;
  error: any;
  password: string;
  newPassword: string;
  confirmNewPassword: string;
  successMessage: string;
  redirectMessage: string;
  submitted = false;

  flag: boolean;


  /**
  * Validation of password        - Password should be within 8-20 characters with at least 
  *                                 one uppercase, one lowercase, one digit and 
  *                                 special character in (!,#,$,%,^,&,*,(,))
  *               NewPassword     - Password should be within 8-20 characters with at least 
  *                                 one uppercase, one lowercase, one digit and 
  *                                 special character in (!,#,$,%,^,&,*,(,))
  *               confirmPassword - Confirm Password should be same as new password
  */
  form = this.fb.group({
    userId: [""],
    password: ["", Validators.required],
    newPassword: [
      "", [Validators.required, PasswordValidator.minLength, PasswordValidator.maxLength,
      PasswordValidator.requiredALowerCase, PasswordValidator.requiredANumber,
      PasswordValidator.requiredASpecialChar, PasswordValidator.requiredAUpperCase]],
    confirmNewPassword: ["", Validators.required]
  });

  /** constructor will be executed on creation of object creation 
    * 
    * the objects specified as parameters will be injected while execution 
    * and these are used as instance variables 
    */
  constructor(public fb: FormBuilder, private changePasswordService: ChangePasswordService, private router: Router,
    private logger: LoggerService, private translateService: TranslateService) { }

  /**
   * Life cycle method on init (overided from OnInit)
   */
  ngOnInit() {

  }
  /**
   * This method validates current password,new password and confirm password
   * If valid it calls changePassword method of ChangePasswordService
   *    which returns a message as a string and redirects to the login page                      
   * If not valid it displays an appropriate error message
   */
  changePasswordSubmit() {
    this.submitted = true;
    this.successMessage = null;
    this.message = null;
    this.flag = false;
    let passwordControl: AbstractControl = this.form.get("password");

    if (!PasswordValidator.minLength(passwordControl) &&
      !PasswordValidator.requiredALowerCase(passwordControl) &&
      !PasswordValidator.requiredANumber(passwordControl) &&
      !PasswordValidator.requiredASpecialChar(passwordControl) &&
      !PasswordValidator.requiredAUpperCase(passwordControl)) {

      this.form.controls['userId'].setValue(sessionStorage.getItem("userId"));
      this.changePasswordService.changePassword(this.form.value).subscribe(
        (responseData: any) => {
          let msg: string = responseData;
          this.successMessage = msg;
          this.translateService.get("OTHER.REDIRECT_CHANGE_PASSWORD").subscribe(value => this.redirectMessage = value);
          this.form.reset();
          this.submitted = false;
          this.logger.info(this.successMessage);
          sessionStorage.clear();
          setTimeout(() => {
            this.router.navigate(["/login"]);
          }, 3000);
        },
        error => {
          error.error = JSON.parse(error.error);
          if (!error.error.message) {
            this.translateService.get("ERROR_MESSAGES.SERVER_DOWN").subscribe(value => this.message = value);
          } else {
            this.message = error.error.message;
          }
          this.logger.error(this.message, error);
          this.submitted = false;
        });
    } else {
      this.logger.warn("password format error");
      this.translateService.get("ERROR_MESSAGES.INVALID_CREDENTIALS").subscribe(value => this.message = value);
      this.submitted = false;
    }
  }

  /** this is a method which redirects from current component to home component */
  cancel() {
    this.router.navigate(['home']);
  }
}