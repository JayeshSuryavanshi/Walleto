import { Component, ChangeDetectionStrategy } from '@angular/core';
import { RouterLink } from '@angular/router';
import { TranslatePipe } from '@ngx-translate/core';

import { IconComponent } from '../../shared/ui/icon.component';
import { WordmarkComponent } from '../../shared/ui/wordmark.component';
import { ThemeToggleComponent } from '../../shared/ui/theme-toggle.component';

@Component({
  selector: 'app-error',
  standalone: true,
  imports: [RouterLink, TranslatePipe, IconComponent, WordmarkComponent, ThemeToggleComponent],
  templateUrl: './error.component.html',
  changeDetection: ChangeDetectionStrategy.Eager,
  styleUrls: ['./error.component.css'],
})
export class ErrorComponent {}
