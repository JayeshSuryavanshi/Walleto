import { Component, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule, FormBuilder, Validators } from '@angular/forms';
import { Router, RouterLink } from '@angular/router';
import { TranslateModule, TranslateService } from '@ngx-translate/core';

import { AuthService } from '../../shared/auth.service';
import { LoggerService } from '../../shared/logger.service';
import { PasswordValidator } from '../../shared/password.validator';
import { IconComponent } from '../../shared/ui/icon.component';
import { WordmarkComponent } from '../../shared/ui/wordmark.component';
import { ThemeToggleComponent } from '../../shared/ui/theme-toggle.component';
import { ChangePasswordService } from './change-password.service';

@Component({
  selector: 'app-change-password',
  standalone: true,
  imports: [
    CommonModule,
    ReactiveFormsModule,
    RouterLink,
    TranslateModule,
    IconComponent,
    WordmarkComponent,
    ThemeToggleComponent,
  ],
  templateUrl: './change-password.component.html',
  styleUrls: ['./change-password.component.css'],
})
export class ChangePasswordComponent {
  private readonly fb = inject(FormBuilder);
  private readonly changePasswordService = inject(ChangePasswordService);
  private readonly router = inject(Router);
  private readonly logger = inject(LoggerService);
  private readonly translate = inject(TranslateService);
  private readonly auth = inject(AuthService);

  message: string | null = null;
  successMessage: string | null = null;
  redirectMessage: string | null = null;
  submitted = false;

  form = this.fb.group({
    password: ['', Validators.required],
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

  changePasswordSubmit(): void {
    this.submitted = true;
    this.successMessage = null;
    this.message = null;

    const passwordControl = this.form.controls.password;
    const currentLooksValid =
      !PasswordValidator.minLength(passwordControl) &&
      !PasswordValidator.requiredALowerCase(passwordControl) &&
      !PasswordValidator.requiredANumber(passwordControl) &&
      !PasswordValidator.requiredASpecialChar(passwordControl) &&
      !PasswordValidator.requiredAUpperCase(passwordControl);

    if (!currentLooksValid) {
      this.logger.warn('password format error');
      this.translate.get('ERROR_MESSAGES.INVALID_CREDENTIALS').subscribe((value) => (this.message = value));
      this.submitted = false;
      return;
    }

    this.changePasswordService
      .changePassword({
        password: this.form.controls.password.value ?? '',
        newPassword: this.form.controls.newPassword.value ?? '',
        confirmNewPassword: this.form.controls.confirmNewPassword.value ?? '',
      })
      .subscribe({
        next: (response) => {
          this.successMessage = response?.message ?? 'Password successfully changed';
          this.translate
            .get('OTHER.REDIRECT_CHANGE_PASSWORD')
            .subscribe((value) => (this.redirectMessage = value));
          this.form.reset();
          this.submitted = false;
          this.logger.info(this.successMessage);
          this.auth.logout();
          setTimeout(() => this.router.navigate(['/login']), 3000);
        },
        error: (error) => {
          this.message = error?.error?.message ?? null;
          if (!this.message) {
            this.translate.get('ERROR_MESSAGES.SERVER_DOWN').subscribe((value) => (this.message = value));
          }
          this.logger.error(this.message ?? 'Change password failed', error);
          this.submitted = false;
        },
      });
  }

  cancel(): void {
    this.router.navigate(['/home']);
  }
}
