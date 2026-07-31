import { ChangeDetectionStrategy, Component, inject } from '@angular/core';

import { ThemeService } from '../theme.service';
import { IconComponent } from './icon.component';

/** Sun/moon inline-SVG button that flips the active theme. */
@Component({
  selector: 'app-theme-toggle',
  standalone: true,
  changeDetection: ChangeDetectionStrategy.OnPush,
  imports: [IconComponent],
  template: `
    <button
      type="button"
      class="icon-btn"
      (click)="theme.toggle()"
      [attr.aria-label]="theme.theme() === 'dark' ? 'Switch to light theme' : 'Switch to dark theme'"
      [attr.aria-pressed]="theme.theme() === 'dark'"
    >
      <app-icon [name]="theme.theme() === 'dark' ? 'sun' : 'moon'"></app-icon>
    </button>
  `,
})
export class ThemeToggleComponent {
  readonly theme = inject(ThemeService);
}
