import { Component, OnInit } from '@angular/core';
import { FormBuilder, Validators } from '@angular/forms';
import { SecurityQuestion } from '../../shared/model/security-question';
import { SecurityQuestionService } from './security-question.service';
import { TranslateService } from '@ngx-translate/core';
import { LoggerService } from '../../shared/logger.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-security-question',
  templateUrl: './security-question.component.html',
  styleUrls: ['./security-question.component.css'],
  providers: [SecurityQuestionService]
})
export class SecurityQuestionComponent implements OnInit {

  constructor(private fb: FormBuilder, private securityQuestionService: SecurityQuestionService, private translateService: TranslateService,
    private logger: LoggerService, private router: Router) { }
  successMessage: string;
  message: string;
  timeout: boolean;
  submitted: boolean;
  securityQuestions: SecurityQuestion[];

  ngOnInit() {

    this.form.get("emailId").setValue(sessionStorage.getItem("emailId"));
    this.form.get("name").setValue(sessionStorage.getItem("name"));
    this.form.get("password").setValue(sessionStorage.getItem("password"));
    this.form.get("mobileNumber").setValue(sessionStorage.getItem("mobileNumber"));
    this.timeout = false;

    this.securityQuestionService.getAllQuestions().subscribe(responseData => {
      this.securityQuestions = responseData;
      /**
       * This code is used to remove the name,emailId,password,mobileNumber
       * from session storage after 15 minutes
       * In case of timeout it shows an appropriate message
       */
      setTimeout(() => {
        sessionStorage.removeItem("emailId");
        sessionStorage.removeItem("name");
        sessionStorage.removeItem("password");
        sessionStorage.removeItem("mobileNumber");
        this.message = "Session time out";
        this.timeout = true;
      }, 900000);
    },
    error => {
      this.translateService.get("ERROR_MESSAGES.SECURITY_QUESTIONS_FETCH_FAILED").subscribe(value => this.message = value);
      this.logger.error(this.message, error);
    });

  }

  form = this.fb.group({
    securityQuestion: this.fb.group({
      questionId: ["", [Validators.required]],
    }),
    securityAnswer: ["", [Validators.required]],
    emailId: ["", Validators.required],
    name: ["", Validators.required],
    password: ["", Validators.required],
    mobileNumber: ["", Validators.required]
  });


  registerWithSecurity() {
    this.submitted = true;
    this.securityQuestionService.register(this.form.value).subscribe(response => {
      this.successMessage = response;
      this.translateService.get("OTHER.REDIRECT1").subscribe(message => this.successMessage+=message);
      setTimeout(() => {
        this.router.navigate(["/login"]);
      }, 4000);
      this.submitted = false;
    }, error => {
      if (error.error.message == null) {
        this.translateService.get("ERROR_MESSAGES.SERVER_DOWN").subscribe(value => this.message = value);
      } else {
        this.message = error.error.message;
      }
      this.submitted = false;
    })
  }

}
