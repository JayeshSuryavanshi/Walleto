import { Component, OnInit, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule, FormBuilder, Validators } from '@angular/forms';
import { Router, RouterLink } from '@angular/router';
import { TranslateModule, TranslateService } from '@ngx-translate/core';

import { LoggerService } from '../../shared/logger.service';
import { PasswordValidator } from '../../shared/password.validator';
import { ForgotPasswordService } from './forgot-password.service';

@Component({
  selector: 'app-forgot-password',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, RouterLink, TranslateModule],
  templateUrl: './forgot-password.component.html',
  styleUrls: ['./forgot-password.component.css'],
})
export class ForgotPasswordComponent implements OnInit {
  private readonly fb = inject(FormBuilder);
  private readonly forgotPasswordService = inject(ForgotPasswordService);
  private readonly logger = inject(LoggerService);
  private readonly translate = inject(TranslateService);
  private readonly router = inject(Router);

  message: string | null = null;
  successMessage: string | null = null;
  redirectMessage: string | null = null;
  submitted = false;
  step = 0;

  /** Recovery context carried between steps (in memory only). */
  question = '';
  private emailId = '';
  private resetToken = '';

  form = this.fb.group({
    emailId: ['', [Validators.required, Validators.pattern('[^@]+[@][^@]+[.][^@]+')]],
  });

  securityForm = this.fb.group({
    securityAnswer: ['', [Validators.required]],
  });

  resetForm = this.fb.group({
    newPassword: [
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
    confirmNewPassword: ['', Validators.required],
  });

  ngOnInit(): void {
    this.step = 0;
  }

  authenticate(): void {
    this.submitted = true;
    this.successMessage = null;
    this.message = null;
    this.emailId = this.form.controls.emailId.value ?? '';

    this.forgotPasswordService.forgotPassword(this.emailId).subscribe({
      next: (response) => {
        this.question = response?.question ?? '';
        this.submitted = false;
        this.step = 1;
      },
      error: (error) => {
        this.setError(error);
        this.submitted = false;
      },
    });
  }

  checkAnswer(): void {
    this.submitted = true;
    this.successMessage = null;
    this.message = null;

    this.forgotPasswordService
      .validateAnswer(this.emailId, this.securityForm.controls.securityAnswer.value ?? '')
      .subscribe({
        next: (response) => {
          this.resetToken = response?.resetToken ?? '';
          this.submitted = false;
          this.step = 2;
        },
        error: (error) => {
          this.setError(error);
          this.submitted = false;
        },
      });
  }

  resetPasswordSubmit(): void {
    this.submitted = true;
    this.successMessage = null;
    this.message = null;

    this.forgotPasswordService
      .resetPassword(
        this.resetForm.controls.newPassword.value ?? '',
        this.resetForm.controls.confirmNewPassword.value ?? '',
        this.resetToken,
      )
      .subscribe({
        next: (response) => {
          this.successMessage = response?.message ?? '';
          this.translate.get('OTHER.REDIRECT2').subscribe((value) => (this.redirectMessage = value));
          this.resetForm.reset();
          this.logger.info('Password reset');
          setTimeout(() => this.router.navigate(['login']), 3000);
        },
        error: (error) => {
          this.setError(error);
          this.submitted = false;
        },
      });
  }

  private setError(error: { error?: { message?: string } }): void {
    if (!error?.error?.message) {
      this.translate.get('ERROR_MESSAGES.SERVER_DOWN').subscribe((value) => (this.message = value));
    } else {
      this.message = error.error.message;
    }
    this.logger.error(this.message ?? 'Recovery failed', error);
  }
}
