/** Routing dimension of a transaction. `paymentType` is 'C' (credit) or 'D' (debit). */
export interface PaymentType {
  paymentTypeId?: number;
  paymentFrom?: string;
  paymentTo?: string;
  paymentType: string;
}
