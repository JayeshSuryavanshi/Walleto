package com.edubank.validator;

import com.edubank.model.DebitCard;

/**
 * The DebitCardValidator class is a utility class having static methods, used
 * to validate the data entered from the form related to debit card. <br>
 * <i>Example:-</i> Debit Card Pin.
 * 
 * @author ETA_JAVA
 *
 */
public class DebitCardValidator {

	/**
	 * This method validates the debit card pin , along while new pin and
	 * confirm new pin while changing the debit card pin. Valid debit card pin
	 * format :- Pin should be of four digit, digits should not be in sequence
	 * 
	 * @param debitCard
	 * 
	 * @throws Exception
	 *             if the current pin is not in proper format or

	 *             if the new pin is not in proper format or 

	 *             if the confirm new pin is not in proper format or 

	 *             if the confirm new pin is not matching new pin.
	 */
	public static void validateChangeDebitCardPin(DebitCard debitCard)
			throws Exception {

		if (!validatePin(debitCard.getPin())) {
			throw new Exception(
					"DebitCardValidator.INVALID_CURRENT_PIN_FORMAT");
		}
		if (!validatePin(debitCard.getNewPin())) {
			throw new Exception(
					"DebitCardValidator.INVALID_NEW_PIN_FORMAT");
		}
		if (!validatePin(debitCard.getConfirmNewPin())) {
			throw new Exception(
					"DebitCardValidator.INVALID_CONFIRM_NEW_PIN_FORMAT");
		}
		if (!debitCard.getNewPin().equals(debitCard.getConfirmNewPin())) {
			throw new Exception(
					"DebitCardValidator.CONFIRM_NEW_PIN_SHOULD_MATCH_NEW_PIN");
		}
	}

	/**
	 * This method validates the debit card pin whether it is in proper format
	 * or not. Valid format:- Pin should be of four digit, digits should not be
	 * in sequence
	 * 
	 * @param pin
	 * 
	 * @return true if the pin is in proper format, else false
	 */
	public static boolean validatePin(String pin) {
		if (pin.matches("[0-9]{4}")) {
			if (!"1234".equals(pin) && !"2345".equals(pin)
					&& !"3456".equals(pin) && !"4567".equals(pin)
					&& !"5678".equals(pin) && !"6789".equals(pin)
					&& !"7890".equals(pin) && !"0123".equals(pin)) {
				return true;
			}
		}

		return false;
	}
}
