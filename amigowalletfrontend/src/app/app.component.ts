import { Component, inject } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { TranslateService } from '@ngx-translate/core';

import { ThemeService } from './shared/theme.service';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [RouterOutlet],
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css'],
})
export class AppComponent {
  // Instantiate the theme service app-wide so the resolved theme stays in sync.
  private readonly theme = inject(ThemeService);

  constructor() {
    // Fallback + active language are declared in appConfig (provideTranslateService).
    // Activate English explicitly using the API-stable `use()`.
    inject(TranslateService).use('en');
  }
}
