package com.amigowallet.utility;

/**
 * The following class contains the constants used throughout 
 * the application for different purposes.
 * 
 * @author ETA_JAVA
 *
 */
public class AmigoWalletConstants {

	public static final String TRANSACTION_INFO_MONEY_CASHBACK_TO_WALLET_CREDIT="money recieved from cashback";
	/**
	 * Constant used for Payment type in transaction 
	 * if amount is being debited
	 */
	public static final String PAYMENT_TYPE_DEBIT = "D";
	
	/**
	 * Constant used for Payment type in transaction 
	 * if amount is being credited
	 */
	public static final String PAYMENT_TYPE_CREDIT = "C";
	
	/**
	 * Constant used for Payment type in transaction 
	 * if amount is being debited from User Wallet
	 */
	public static final String PAYMENT_FROM_WALLET = "W";
	
	/**
	 * Constant used for Payment type in transaction 
	 * if amount is being debited from the bank
	 */
	public static final String PAYMENT_FROM_BANK = "B";
	
	/**
	 * Constant used for Payment type in transaction 
	 * if amount is being debited from Merchant Wallet
	 */
	public static final String PAYMENT_FROM_MERCHANT = "M";

	/**
	 * Constant used for Payment type in transaction 
	 * if amount is being credited to User Wallet
	 */
	public static final String PAYMENT_TO_WALLET = "W";
	
	/**
	 * Constant used for Payment type in transaction 
	 * if amount is being credited to the Bank
	 */
	public static final String PAYMENT_TO_BANK = "B";
	
	/**
	 * Constant used for Payment type in transaction 
	 * if amount is being credited to Merchant Wallet
	 */
	public static final String PAYMENT_TO_MERCHANT = "M";

	/**
	 * Constant used for Reward points has been 
	 * redeemed for a transaction
	 */
	public static final String REWARD_POINTS_REDEEMED_YES = "Y";
	
	/**
	 * Constant used for Reward points has not been 
	 * redeemed for a transaction
	 */
	public static final String REWARD_POINTS_REDEEMED_NO = "N";
	
	/**
	 * Constant used for storing the information of a 
	 * transaction if an amount is added to wallet by 
	 * redeeming the reward points 
	 */
	public static final String TRANSACTION_INFO_MONEY_ADDED_BY_REDEEMING_REWARD_POINTS = 
								"money credited to wallet by redeeming reward points";
	
	/**
	 * Constant used for storing the information of a 
	 * transaction if an amount is added to wallet 
	 * from bank using a debit card 
	 */
	public static final String TRANSACTION_INFO_MONEY_ADDED_FROM_BANK_TO_EWALLET_USING_DEBIT_CARD = 
								"money credited from bank using debit card";
	
	/**
	 * Constant used for storing the information of a 
	 * transaction if an amount is added to wallet 
	 * from bank using Internet banking 
	 */
	public static final String TRANSACTION_INFO_MONEY_ADDED_FROM_BANK_TO_EWALLET_USING_NET_BANKING = 
								"money credited from bank using net banking";
	
	/**
	 * Constant used for storing the information of a 
	 * transaction if an amount is sent from wallet 
	 * to bank account
	 */
	public static final String TRANSACTION_INFO_MONEY_SENT_TO_BANK_ACCOUNT_FROM_EWALLET = 
								"money debited from e-wallet and trasferred to bank account";
		
	/**
	 * Constant used to calculate the amount out of reward 
	 * points while redeeming the points 
	 */
	public static final Double REDEEM_PERCENTAGE = 0.10d;
	
	/**
	 * Constant used for storing the information of a 
	 * transaction if an amount is been sent  
	 * to another wallet
	 */
	public static final String TRANSACTION_INFO_MONEY_WALLET_TO_WALLET_DEBIT="money sent to ";
	
	/**
	 * Constant used for storing the information of a 
	 * transaction if an amount is been received  
	 * from another wallet
	 */
	public static final String TRANSACTION_INFO_MONEY_WALLET_TO_WALLET_CREDIT="money recieved from ";
	
	/**
	 * Constant used for storing the information of a 
	 * transaction if an amount is been paid to a merchant  
	 */
	public static final String TRANSACTION_INFO_MONEY_WALLET_TO_MERCHANTPAYMENT_DEBIT="money paid to ";

	/**
	 * Constant used for storing the information of a 
	 * transaction if an amount is been sent to Bank
	 */
	public static final String TRANSACTION_INFO_PAY_TO_BANK="money deposited to bank";
	
	/**
	 * Constant to check if a OPT is valid
	 */
	public static final String ISVALID = "Y";
	
	/**
	 * Constant to check the purpose of an OTP
	 */
	public static final String PURPOSE="Registration";

	/**
	 * Constant to store the information that user has 
	 * paid to a merchant for Water Bill
	 */
	public static final String MERCHANT_SERVICE_TYPE_WATER_BILL="Water Bill Payment";
	
	/**
	 * Constant to store the information that user has 
	 * paid to a merchant for Electricity Bill
	 */
	public static final String MERCHANT_SERVICE_TYPE_ELECTRIC_BILL="Electricity Bill Payment";
	
	/**
	 * Constant to store the information that user has 
	 * paid to a merchant for LandLine Bill
	 */
	public static final String MERCHANT_SERVICE_TYPE_LANDLINE_BILL="Landline Bill Payment";
	
	/**
	 * Constant to store the information that user has 
	 * paid to a merchant for Post-paid Bill
	 */
	public static final String MERCHANT_SERVICE_TYPE_POSTPAID_BILL="Postpaid Mobile Bill Payment";
	
	/**
	 * Constant to store the information that user has 
	 * paid to a merchant for Data Bill
	 */
	public static final String MERCHANT_SERVICE_TYPE_DATA_BILL="Postpaid Data Card Bill Payment";
	
	/**
	 * Constant to store the information that user has 
	 * paid to a merchant for Broadband Bill
	 */
	public static final String MERCHANT_SERVICE_TYPE_BROADBAND_BILL="Brodband Bill Payment";
	
	/**
	 * Constant to store the information that user has 
	 * paid to a merchant for Gas Bill
	 */
	public static final String MERCHANT_SERVICE_TYPE_GAS_BILL="Gas Bill Payment";
}
