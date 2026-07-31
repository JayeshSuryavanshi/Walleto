import { Component, OnInit, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule, FormBuilder, Validators } from '@angular/forms';
import { Router, RouterLink } from '@angular/router';
import { TranslateModule, TranslateService } from '@ngx-translate/core';

import { LoggerService } from '../../shared/logger.service';
import { SecurityQuestion } from '../../shared/model/security-question';
import { RegisterRequest } from '../../shared/model/api';
import { RegistrationStateService } from '../register/registration-state.service';
import { WordmarkComponent } from '../../shared/ui/wordmark.component';
import { ThemeToggleComponent } from '../../shared/ui/theme-toggle.component';
import { SecurityQuestionService } from './security-question.service';

@Component({
  selector: 'app-security-question',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, RouterLink, TranslateModule, WordmarkComponent, ThemeToggleComponent],
  templateUrl: './security-question.component.html',
  styleUrls: ['./security-question.component.css'],
})
export class SecurityQuestionComponent implements OnInit {
  private readonly fb = inject(FormBuilder);
  private readonly securityQuestionService = inject(SecurityQuestionService);
  private readonly translate = inject(TranslateService);
  private readonly logger = inject(LoggerService);
  private readonly router = inject(Router);
  private readonly registrationState = inject(RegistrationStateService);

  successMessage: string | null = null;
  message: string | null = null;
  timeout = false;
  submitted = false;
  securityQuestions: SecurityQuestion[] = [];

  form = this.fb.group({
    securityQuestion: this.fb.group({
      questionId: ['', [Validators.required]],
    }),
    securityAnswer: ['', [Validators.required]],
  });

  ngOnInit(): void {
    // Guard against direct navigation / reload with no pending registration.
    if (this.registrationState.get() == null) {
      this.router.navigate(['/login']);
      return;
    }

    this.securityQuestionService.getAllQuestions().subscribe({
      next: (questions) => {
        this.securityQuestions = questions;
        // Expire the in-memory pending registration after 15 minutes.
        setTimeout(
          () => {
            this.registrationState.clear();
            this.message = 'Session time out';
            this.timeout = true;
          },
          15 * 60 * 1000,
        );
      },
      error: (error) => {
        this.translate
          .get('ERROR_MESSAGES.SECURITY_QUESTIONS_FETCH_FAILED')
          .subscribe((value) => (this.message = value));
        this.logger.error(this.message ?? 'Failed to fetch security questions', error);
      },
    });
  }

  registerWithSecurity(): void {
    const pending = this.registrationState.get();
    if (pending == null) {
      this.router.navigate(['/login']);
      return;
    }

    this.submitted = true;
    const request: RegisterRequest = {
      ...pending,
      securityQuestion: { questionId: Number(this.form.controls.securityQuestion.controls.questionId.value) },
      securityAnswer: this.form.controls.securityAnswer.value ?? '',
    };

    this.securityQuestionService.register(request).subscribe({
      next: (response) => {
        this.registrationState.clear();
        this.successMessage = response?.message ?? '';
        this.translate.get('OTHER.REDIRECT1').subscribe((redirect) => (this.successMessage += redirect));
        setTimeout(() => this.router.navigate(['/login']), 4000);
        this.submitted = false;
      },
      error: (error) => {
        if (error?.error?.message == null) {
          this.translate.get('ERROR_MESSAGES.SERVER_DOWN').subscribe((value) => (this.message = value));
        } else {
          this.message = error.error.message;
        }
        this.submitted = false;
      },
    });
  }
}
