import { Component, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router } from '@angular/router';
import { TranslateModule } from '@ngx-translate/core';

import { AuthService } from '../../shared/auth.service';

/** Profile bar: name, balance, reward points, change-password and logout. */
@Component({
  selector: 'app-profile',
  standalone: true,
  imports: [CommonModule, TranslateModule],
  templateUrl: './profile.component.html',
  styleUrls: ['./profile.component.css'],
})
export class ProfileComponent {
  readonly auth = inject(AuthService);
  private readonly router = inject(Router);

  changePassword(): void {
    this.router.navigate(['/changePassword']);
  }

  logOut(): void {
    this.auth.logout();
    this.router.navigate(['/login']);
  }
}
