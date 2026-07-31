import { Component, OnInit, inject, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule, FormBuilder, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { TranslateModule, TranslateService } from '@ngx-translate/core';

import { LoggerService } from '../../shared/logger.service';
import { PasswordValidator } from '../../shared/password.validator';
import { RegisterRequest } from '../../shared/model/api';
import { IconComponent } from '../../shared/ui/icon.component';

import { RegisterService } from './register.service';
import { CaptchaService } from './captcha.service';
import { RegistrationStateService } from './registration-state.service';

@Component({
  selector: 'app-register',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, TranslateModule, IconComponent],
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.css'],
  providers: [CaptchaService],
})
export class RegisterComponent implements OnInit {
  private readonly fb = inject(FormBuilder);
  private readonly registerService = inject(RegisterService);
  private readonly router = inject(Router);
  private readonly logger = inject(LoggerService);
  private readonly translate = inject(TranslateService);
  private readonly registrationState = inject(RegistrationStateService);
  readonly captchaService = inject(CaptchaService);

  successMessage: string | null = null;
  message: string | null = null;
  submitted = false;
  readonly showPassword = signal(false);
  readonly showConfirm = signal(false);

  form1 = this.fb.group({
    name: ['', [Validators.required, Validators.pattern('[A-Za-z]+([ ][A-Za-z]+)*')]],
    password: [
      '',
      [
        Validators.required,
        PasswordValidator.minLength,
        PasswordValidator.maxLength,
        PasswordValidator.requiredALowerCase,
        PasswordValidator.requiredANumber,
        PasswordValidator.requiredASpecialChar,
        PasswordValidator.requiredAUpperCase,
      ],
    ],
    emailId: ['', [Validators.required, Validators.pattern('[^@]+[@][^@]+[.][^@]+')]],
    mobileNumber: ['', [Validators.required, Validators.pattern('[0-9]{10}')]],
    confirmPassword: ['', [Validators.required]],
    notARobot: ['', Validators.required],
  });

  ngOnInit(): void {
    this.captchaService.selectCaptchaRandom();
  }

  register(): void {
    this.successMessage = null;
    this.message = null;
    this.submitted = true;

    if (!this.captchaService.checkCaptha(this.form1.controls.notARobot.value ?? '')) {
      this.translate.get('ERROR_MESSAGES.CAPTCHA_NOT_MATCHED').subscribe((value) => (this.message = value));
      this.logger.error(this.message ?? 'Captcha not matched');
      this.submitted = false;
      return;
    }

    const request: RegisterRequest = {
      name: this.form1.controls.name.value ?? '',
      emailId: this.form1.controls.emailId.value ?? '',
      mobileNumber: this.form1.controls.mobileNumber.value ?? '',
      password: this.form1.controls.password.value ?? '',
    };

    this.registerService.validateForRegistration(request).subscribe({
      next: (response) => {
        // Hold the validated signup in memory (never on disk) for step 2.
        this.registrationState.set(request);
        this.successMessage = response?.message ?? null;
        this.form1.reset();
        this.logger.info('Registration in process');
        setTimeout(() => this.router.navigate(['/security']), 4000);
      },
      error: (error) => {
        if (error?.error?.message == null) {
          this.translate.get('ERROR_MESSAGES.SERVER_DOWN').subscribe((value) => (this.message = value));
        } else {
          this.message = error.error.message;
        }
        this.logger.error(this.message ?? 'Registration failed', error);
        this.submitted = false;
      },
    });
  }
}
