import { Component, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { animate, keyframes, style, transition, trigger } from '@angular/animations';
import { TranslateModule } from '@ngx-translate/core';

import { AuthService } from '../../shared/auth.service';
import { LoggerService } from '../../shared/logger.service';
import { extractApiError, friendlyMoneyResult } from '../../shared/money-format';
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
  readonly auth = inject(AuthService);
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
        this.successMessage = friendlyMoneyResult(response, { title: 'Points redeemed', verb: 'added' });
        this.auth.applyMoneyResult(response);
        this.submitted = false;
        this.logger.info(this.successMessage);
      },
      error: (error) => {
        this.errorMessage = extractApiError(error, 'Could not redeem points. Please try again.');
        this.submitted = false;
        this.logger.error(this.errorMessage ?? 'Redeem failed', error);
      },
    });
  }
}
