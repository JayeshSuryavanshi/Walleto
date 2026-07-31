import { PaymentType } from './payment-type';

/** A ledger row in the wallet transaction history. */
export interface UserTransaction {
  userTransactionId: number;
  amount: number;
  transactionDateTime: string;
  remarks?: string;
  info?: string;
  pointsEarned?: number;
  isRedeemed?: string;
  message?: string;
  paymentType: PaymentType;
  transactionStatus?: string;
}
