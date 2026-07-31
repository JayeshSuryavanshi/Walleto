/**
 * Uniform response returned by every money-mutation endpoint on the wallet-api.
 *
 * `newBalance` is the authoritative post-transaction wallet balance.
 * `pointsEarned` and `bankTransactionId` are present only where relevant
 * (the backend omits them - `@JsonInclude(NON_NULL)` - otherwise), and
 * `message` may be absent for some flows.
 */
export interface MoneyTransactionResponse {
  message?: string;
  amount: number;
  newBalance: number;
  pointsEarned?: number;
  bankTransactionId?: string;
}
