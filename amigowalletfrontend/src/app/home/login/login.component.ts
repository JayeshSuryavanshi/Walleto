import { Component, inject, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule, FormBuilder, Validators } from '@angular/forms';
import { Router, RouterLink } from '@angular/router';
import { TranslateModule, TranslateService } from '@ngx-translate/core';

import { AuthService } from '../../shared/auth.service';
import { LoggerService } from '../../shared/logger.service';
import { PasswordValidator } from '../../shared/password.validator';
import { IconComponent } from '../../shared/ui/icon.component';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, RouterLink, TranslateModule, IconComponent],
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css'],
})
export class LoginComponent {
  private readonly fb = inject(FormBuilder);
  private readonly auth = inject(AuthService);
  private readonly router = inject(Router);
  private readonly logger = inject(LoggerService);
  private readonly translate = inject(TranslateService);

  message: string | null = null;
  submitted = false;
  readonly showPassword = signal(false);

  form = this.fb.group({
    emailId: ['', [Validators.required, Validators.pattern('[^@]+[@][^@]+[.][^@]+')]],
    password: ['', Validators.required],
  });

  authenticate(): void {
    this.submitted = true;
    this.message = null;

    const passwordControl = this.form.controls.password;
    const passwordLooksValid =
      !PasswordValidator.minLength(passwordControl) &&
      !PasswordValidator.requiredALowerCase(passwordControl) &&
      !PasswordValidator.requiredANumber(passwordControl) &&
      !PasswordValidator.requiredASpecialChar(passwordControl) &&
      !PasswordValidator.requiredAUpperCase(passwordControl);

    if (!passwordLooksValid) {
      this.logger.warn('Password format error');
      this.translate.get('ERROR_MESSAGES.INVALID_CREDENTIALS').subscribe((value) => (this.message = value));
      this.submitted = false;
      return;
    }

    this.auth
      .login({
        emailId: this.form.controls.emailId.value ?? '',
        password: this.form.controls.password.value ?? '',
      })
      .subscribe({
        next: () => {
          this.router.navigate(['/home']);
          this.logger.info('Successful login');
        },
        error: (error) => {
          if (error?.error?.message != null) {
            this.message = error.error.message;
          } else {
            this.translate.get('ERROR_MESSAGES.SERVER_DOWN').subscribe((value) => (this.message = value));
          }
          this.logger.error(this.message ?? 'Login failed', error);
          this.submitted = false;
        },
      });
  }
}
