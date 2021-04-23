import { Bank } from './bank';
/**
 * This is a model class which has the attributes to keep Card properties
 */
export class Card {
    public cardId: number;
    public cardNumber: string;
    public expiryDate: Date;
    public status: string;
    public successMessage: string;
    public errorMessage: string;
    public userName: string;
    public cvv: string;
    public bank: Bank;
    public pin: string;
}