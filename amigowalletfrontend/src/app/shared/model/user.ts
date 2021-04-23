import {Card} from './card';
import {UserTransaction} from './user-transaction';
import { SecurityQuestion } from './security-question';

/**
 * This is a model class which has the attributes to keep User properties
 */
export class User {
    
    public userId: number;
	public emailId: string;
	public mobileNumber: string;
	public name: string;
	public password: string;
	public message: string;
	public balance: number;
	public userTransactions: UserTransaction[];
	public cards: Card[];
	public rewardPoints: number;
    public otp: string;
	public successMessage: string;
	public errorMessage: string;
	public securityQuestion: SecurityQuestion;
	public securityAnswer: string;
}