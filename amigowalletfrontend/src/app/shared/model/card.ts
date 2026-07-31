/**
 * A saved card as returned inside the authenticated user profile.
 * The wallet-api never exposes the full PAN any more - only a masked number.
 */
export interface CardInfo {
  cardId: number;
  maskedCardNumber: string;
  bankName: string;
  expiryDate: string;
  cardStatus: string;
}
