import { Component, inject } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { TranslateService } from '@ngx-translate/core';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [RouterOutlet],
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css'],
})
export class AppComponent {
  constructor() {
    // Fallback + active language are declared in appConfig (provideTranslateService).
    // Activate English explicitly using the API-stable `use()`.
    inject(TranslateService).use('en');
  }
}
