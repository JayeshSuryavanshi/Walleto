import { Component, OnInit } from '@angular/core';
import { FormBuilder, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { User } from '../../shared/model/user';

import { TranslateService } from '@ngx-translate/core';
import { LoggerService } from '../../shared/logger.service';
import { PasswordValidator } from '../../shared/password.validator';

import { RegisterService } from './register.service';
import { CaptchaService } from './captcha.service';

// This component is used by new user for registering

/** Annotation which specifes this as a component 
 * 
 * selector: defines a tag which can be used in any html as <aw-register></aw-register>
 * moduleId: This specified for specifing that the path used in the component are relative to this component
 * templateUrl: This is the relative path for the html related to this component
 * styleUrls: This is the relative path for the css related to this component
 * providers: specifies the service class which communicates with the Serverside application
 *            specifing here makes it available only in this class
*/
@Component({
  selector: 'aw-register',
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.css'],
  providers: [RegisterService, CaptchaService]
})
export class RegisterComponent implements OnInit {
  // listOfSecurity: SecurityQuestion[];

  /** class instance variales */
  user: User;
  successMessage: any;
  message: any;
  error: any;
  submitted = false;

  /** key required for reCaptcha */
  // recaptchaSiteKey = '6LdzZiwUAAAAAMkkzz3y9GW-NUFetCRWexVfGmiI';

  /**
   * Validation of name            - Name will allow only alphabets and spaces
   *               password        - Password should be within 8-20 characters with at least 
   *                                 one uppercase, one lowercase, one digit and 
	 *                                 special character in (!,#,$,%,^,&,*,(,))
   *               emailId         - Email id should follow a valid format
   *               mobileNumber    - Mobile number should be 10 digits
   *               confirmPassword - Confirm Password should be same as password
   *               notARobot       - reCaptcha success
   */
  form1 = this.fb.group({
    name: ["", [Validators.required, Validators.pattern("[A-Za-z]+([ ][A-Za-z]+)*")]],
    password: ["", [Validators.required, PasswordValidator.minLength, PasswordValidator.maxLength,
    PasswordValidator.requiredALowerCase, PasswordValidator.requiredANumber,
    PasswordValidator.requiredASpecialChar, PasswordValidator.requiredAUpperCase]],
    emailId: ["", [Validators.required, Validators.pattern("[^@]+[@][^@]+[.][^@]+")]],
    mobileNumber: ["", [Validators.required, Validators.pattern("[0-9]{10}")]],
    confirmPassword: ["", [Validators.required]],
    notARobot: ["", Validators.required]
  });

  /** constructor will be executed on creation of object creation 
   * 
   * the objects specified as parameters will be injected while execution 
   * and these are used as instance variables 
   */
  constructor(public fb: FormBuilder, private registerService: RegisterService, private router: Router,
    private logger: LoggerService, private translateService: TranslateService, public captchaService: CaptchaService) { }

  /**
   * Life cycle method on init (overided from OnInit)
   */
  ngOnInit() {
    this.captchaService.selectCaptchaRandom();
    // // this.captchaService.getSelectedCaptchaImagePath()
    // this.registerService.getAllSecurityQuestions()
    // .then(listOfSecurity => this.listOfSecurity = listOfSecurity)
    // .catch(error => {
    //   if (error.errorMessage != null) {
    //     this.message = error.errorMessage;
    //   } else {
    //     this.translateService.get("ERROR_MESSAGES.SERVER_DOWN").subscribe(value => this.message = value);
    //   }});
  }

  /** on reCaptcha success, google calls this method with a response */
  // onCaptchaComplete(response: any) {
  //   this.form1.controls["notARobot"].setValue("success");
  // }

  /**
   * This method calls doRegister method of RegisterService
   * which returns a user model populated with appropriate
   * message.
   * Name,emailId,password and mobileNumber are stored in sessionStorage
   * In case of success it redirects to otp page
   * In case of failure it dispalys an error message
   */
  register() {
    this.successMessage = null;
    this.message = null;
    this.submitted = true;
    if (this.captchaService.checkCaptha(this.form1.controls.notARobot.value)) {
      this.registerService.doRegister(this.form1.value).subscribe((responseData: any) => {
        let user: User = responseData;
        this.user = user;
        sessionStorage.setItem("emailId", this.user.emailId);
        sessionStorage.setItem("name", this.user.name);
        sessionStorage.setItem("password", this.user.password);
        sessionStorage.setItem("mobileNumber", this.user.mobileNumber);
        this.successMessage = this.user.successMessage;
        this.form1.reset();
        this.logger.info("Registration in process");
        setTimeout(() => {
          this.router.navigate(["/security"]);
        }, 4000);
      }, error => {
        if (error.error.message == null) {
          this.translateService.get("ERROR_MESSAGES.SERVER_DOWN").subscribe(value => this.message = value);
        } else {
          this.message = error.error.message;
        }
        this.logger.error(this.message, error);
        this.submitted = false;
      });
    } else {
      this.translateService.get("ERROR_MESSAGES.CAPTCHA_NOT_MATCHED").subscribe(value => this.message = value);
      this.logger.error(this.message);
      this.submitted = false;
    }
  }
} 