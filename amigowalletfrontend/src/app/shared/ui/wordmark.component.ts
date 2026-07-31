import { ChangeDetectionStrategy, Component, Input } from '@angular/core';

/**
 * Walleto wordmark: a rounded-square "W" mark in --accent alongside the
 * name set in Space Grotesk 700. Minimal and confident.
 */
@Component({
  selector: 'app-wordmark',
  standalone: true,
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <span class="wm" [class.wm--lg]="size === 'lg'">
      <span class="wm-mark" aria-hidden="true">W</span>
      @if (showName) {
        <span class="wm-name">Walleto</span>
      }
    </span>
  `,
  styles: [
    `
      .wm {
        display: inline-flex;
        align-items: center;
        gap: 10px;
        user-select: none;
        line-height: 1;
      }
      .wm-mark {
        display: inline-grid;
        place-items: center;
        width: 28px;
        height: 28px;
        border-radius: 8px;
        background: var(--accent);
        color: var(--accent-ink);
        font-family: var(--font-display);
        font-weight: 700;
        font-size: 1rem;
        letter-spacing: -0.02em;
      }
      .wm-name {
        font-family: var(--font-display);
        font-weight: 700;
        font-size: 1.25rem;
        letter-spacing: -0.02em;
        color: var(--ink);
      }
      .wm--lg .wm-mark {
        width: 40px;
        height: 40px;
        border-radius: 11px;
        font-size: 1.4rem;
      }
      .wm--lg .wm-name {
        font-size: 1.75rem;
      }
    `,
  ],
})
export class WordmarkComponent {
  @Input() showName = true;
  @Input() size: 'md' | 'lg' = 'md';
}
