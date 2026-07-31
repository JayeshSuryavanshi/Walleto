import { MoneyTransactionResponse } from './model/money-transaction-response';

const USD = new Intl.NumberFormat('en-US', {
  style: 'currency',
  currency: 'USD',
  minimumFractionDigits: 2,
  maximumFractionDigits: 2,
});

/** Format a number as a USD currency string, or '' when not a real number. */
export function formatUsd(value: number | null | undefined): string {
  return typeof value === 'number' && Number.isFinite(value) ? USD.format(value) : '';
}

export interface MoneyResultOptions {
  /** Lead phrase, e.g. "Transfer successful". */
  title: string;
  /** Past-tense verb describing what happened to the amount, e.g. "sent". */
  verb?: string;
}

/**
 * Build a friendly, single-line confirmation from a `MoneyTransactionResponse`,
 * e.g. "✓ Transfer successful — $250.00 sent. New balance $3,218.60 · +2 points."
 * Only the fields the backend actually returned are shown.
 */
export function friendlyMoneyResult(
  res: MoneyTransactionResponse | null | undefined,
  opts: MoneyResultOptions,
): string {
  const amount = formatUsd(res?.amount);
  const balance = formatUsd(res?.newBalance);

  let text = `✓ ${opts.title}`;
  text += amount ? ` — ${amount}${opts.verb ? ' ' + opts.verb : ''}.` : '.';

  const tail: string[] = [];
  if (balance) {
    tail.push(`New balance ${balance}`);
  }
  if (typeof res?.pointsEarned === 'number' && res.pointsEarned > 0) {
    tail.push(`+${res.pointsEarned} point${res.pointsEarned === 1 ? '' : 's'}`);
  }
  if (res?.bankTransactionId) {
    tail.push(`Ref ${res.bankTransactionId}`);
  }
  if (tail.length > 0) {
    text += ` ${tail.join(' · ')}.`;
  }
  return text;
}

/**
 * Extract a human-friendly error message from a failed HTTP call. Prefers the
 * `message` field of the wallet-api `ApiError` body (whether delivered as a
 * parsed object or a raw JSON string) and never surfaces raw JSON to the user.
 */
export function extractApiError(
  error: unknown,
  fallback = 'Something went wrong. Please try again.',
): string {
  const body = (error as { error?: unknown } | null | undefined)?.error;

  if (typeof body === 'string') {
    const trimmed = body.trim();
    try {
      const parsed = JSON.parse(trimmed) as { message?: string };
      return parsed?.message?.trim() || fallback;
    } catch {
      // Not JSON: show the plain text if it looks like a real sentence.
      return trimmed && !trimmed.startsWith('{') ? trimmed : fallback;
    }
  }

  if (body && typeof body === 'object' && 'message' in body) {
    const message = (body as { message?: string }).message;
    return message?.trim() || fallback;
  }

  return fallback;
}
