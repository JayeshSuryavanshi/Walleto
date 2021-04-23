import { PaymentType } from './payment-type';
/**
 * This is a model class which has the attributes to keep UserTransaction properties
 */
export class UserTransaction {
    public userTransactionId: number;
    public amount: number;
    public transactionDateTime: Date;
    public remarks: string;
    public info: string;
    public pointsEarned: number;
    public isRedeemed: string;
    public message: string;
    public paymentType: PaymentType;
    public transactionStatus: string;
}