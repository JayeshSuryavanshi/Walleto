import { Component } from '@angular/core';
import { RouterLink } from '@angular/router';
import { TranslateModule } from '@ngx-translate/core';

import { IconComponent } from '../../shared/ui/icon.component';
import { WordmarkComponent } from '../../shared/ui/wordmark.component';
import { ThemeToggleComponent } from '../../shared/ui/theme-toggle.component';

@Component({
  selector: 'app-error',
  standalone: true,
  imports: [RouterLink, TranslateModule, IconComponent, WordmarkComponent, ThemeToggleComponent],
  templateUrl: './error.component.html',
  styleUrls: ['./error.component.css'],
})
export class ErrorComponent {}
