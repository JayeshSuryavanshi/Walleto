import { CardInfo } from './card';

/**
 * The authenticated user profile returned by `/UserLoginAPI/authenticate`
 * (inside `user`) and by `/UserLoginAPI/getUser`.
 *
 * NOTE: unlike the legacy app, this NEVER carries a password or security answer.
 */
export interface UserProfile {
  userId: number;
  name: string;
  emailId: string;
  mobileNumber: string;
  userStatus: string;
  balance: number;
  rewardPoints: number;
  cards: CardInfo[];
}
