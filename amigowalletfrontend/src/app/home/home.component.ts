import { Component, signal } from '@angular/core';
import { TranslateModule } from '@ngx-translate/core';

import { LoginComponent } from './login/login.component';
import { RegisterComponent } from './register/register.component';
import { WordmarkComponent } from '../shared/ui/wordmark.component';
import { ThemeToggleComponent } from '../shared/ui/theme-toggle.component';

type AuthMode = 'signin' | 'create';

/**
 * Auth entry screen. A single, focused card that switches between sign-in and
 * create-account (replacing the old side-by-side login + register cram).
 */
@Component({
  selector: 'app-home',
  standalone: true,
  imports: [TranslateModule, LoginComponent, RegisterComponent, WordmarkComponent, ThemeToggleComponent],
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.css'],
})
export class HomeComponent {
  readonly mode = signal<AuthMode>('signin');
}
