import { Component, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { animate, keyframes, style, transition, trigger } from '@angular/animations';
import { TranslateModule, TranslateService } from '@ngx-translate/core';

import { AuthService } from '../../shared/auth.service';
import { LoggerService } from '../../shared/logger.service';
import { PointsService } from './points.service';

@Component({
  selector: 'app-points',
  standalone: true,
  imports: [CommonModule, TranslateModule],
  templateUrl: './points.component.html',
  styleUrls: ['./points.component.css'],
  animations: [
    trigger('loadAnimation', [
      transition('void => *', [
        animate(
          '1000ms ease-out',
          keyframes([style({ opacity: 0, offset: 0 }), style({ opacity: 1, offset: 1 })]),
        ),
      ]),
    ]),
  ],
})
export class PointsComponent {
  private readonly pointService = inject(PointsService);
  private readonly translate = inject(TranslateService);
  private readonly auth = inject(AuthService);
  private readonly logger = inject(LoggerService);

  state = 'active';
  successMessage: string | null = null;
  errorMessage: string | null = null;
  submitted = false;

  redeem(): void {
    this.submitted = true;
    this.successMessage = null;
    this.errorMessage = null;
    this.pointService.redeem().subscribe({
      next: (response) => {
        this.successMessage = response?.successMessage ?? response?.message ?? 'Reward points redeemed';
        this.auth.refreshProfile().subscribe({ error: () => undefined });
        this.submitted = false;
        this.logger.info(this.successMessage);
      },
      error: (error) => {
        if (error?.error?.message != null) {
          this.errorMessage = error.error.message;
        } else {
          this.translate.get('ERROR_MESSAGES.SERVER_DOWN').subscribe((value) => (this.errorMessage = value));
        }
        this.submitted = false;
        this.logger.error(this.errorMessage ?? 'Redeem failed', error);
      },
    });
  }
}
