package com.edubank.validator.test;

import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import com.edubank.model.DebitCard;
import com.edubank.validator.DebitCardValidator;

/**
 * This is a JUnit test class to test the methods of {@link DebitCardValidator}
 *
 * @author ETA_JAVA
 */
public class DebitCardValidatorTest {

	/**
	 * Creating expected exception rule for testing exceptions in negative test
	 * case methods.
	 */
	@Rule
	public ExpectedException expectedException = ExpectedException.none();

	/**
	 * This is a negative test case method to test
	 * {@link DebitCardValidator#validateChangeDebitCardPin(DebitCard)} method
	 * 
	 */
	@Test
	public void validateChangeDebitCardPinInvalidCurentPinFormat() throws Exception {
		DebitCard debitCard = new DebitCard();
		debitCard.setPin("abd");
		debitCard.setNewPin("4544");
		debitCard.setConfirmNewPin("4544");

		/*
		 * Populating expected exception rule to expect exceptions of Exception
		 * type with a specific message.
		 */
		expectedException.expect(Exception.class);
		expectedException.expectMessage("DebitCardValidator.INVALID_CURRENT_PIN_FORMAT");

		DebitCardValidator.validateChangeDebitCardPin(debitCard);
	}

	/**
	 * This is a negative test case method to test
	 * {@link DebitCardValidator#validateChangeDebitCardPin(DebitCard)} method
	 * 
	 */
	@Test
	public void validateChangeDebitCardPinInvalidNewPinFormat() throws Exception {
		DebitCard debitCard = new DebitCard();
		debitCard.setPin("2634");
		debitCard.setNewPin("4 44");
		debitCard.setConfirmNewPin("4544");

		/*
		 * Populating expected exception rule to expect exceptions of Exception
		 * type with a specific message.
		 */
		expectedException.expect(Exception.class);
		expectedException.expectMessage("DebitCardValidator.INVALID_NEW_PIN_FORMAT");

		DebitCardValidator.validateChangeDebitCardPin(debitCard);
	}

	/**
	 * This is a negative test case method to test
	 * {@link DebitCardValidator#validateChangeDebitCardPin(DebitCard)} method
	 * 
	 */
	@Test
	public void validateChangeDebitCardPinInvalidConfirmNewPinFormat() throws Exception {
		DebitCard debitCard = new DebitCard();
		debitCard.setPin("2634");
		debitCard.setNewPin("4544");
		debitCard.setConfirmNewPin("6789");

		/*
		 * Populating expected exception rule to expect exceptions of Exception
		 * type with a specific message.
		 */
		expectedException.expect(Exception.class);
		expectedException.expectMessage("DebitCardValidator.INVALID_CONFIRM_NEW_PIN_FORMAT");

		DebitCardValidator.validateChangeDebitCardPin(debitCard);
	}

	/**
	 * This is a negative test case method to test
	 * {@link DebitCardValidator#validateChangeDebitCardPin(DebitCard)} method
	 * 
	 */

	@Test
	public void validateChangeDebitCardPinConfirmPinMismatchNewPin() throws Exception {
		DebitCard debitCard = new DebitCard();
		debitCard.setPin("2634");
		debitCard.setNewPin("4544");
		debitCard.setConfirmNewPin("6633");

		/*
		 * Populating expected exception rule to expect exceptions of Exception
		 * type with a specific message.
		 */
		expectedException.expect(Exception.class);
		expectedException.expectMessage("DebitCardValidator.CONFIRM_NEW_PIN_SHOULD_MATCH_NEW_PIN");

		DebitCardValidator.validateChangeDebitCardPin(debitCard);
	}

	/**
	 * This is a negative test case method to test
	 * {@link DebitCardValidator#validatePin(String)} method
	 * 
	 */
	@Test
	public void validatePinInvalidPin1() {
		boolean result = DebitCardValidator.validatePin("A");
		Assert.assertFalse(result);
	}

	/**
	 * This is a negative test case method to test
	 * {@link DebitCardValidator#validatePin(String)} method
	 * 
	 */
	@Test
	public void validatePinInvalidPin2() {
		boolean result = DebitCardValidator.validatePin("0052154");
		Assert.assertFalse(result);
	}

	/**
	 * This is a negative test case method to test
	 * {@link DebitCardValidator#validatePin(String)} method
	 * 
	 */
	@Test
	public void validatePinInvalidPin3() {
		boolean result = DebitCardValidator.validatePin("asdf");
		Assert.assertFalse(result);
	}

	/**
	 * This is a negative test case method to test
	 * {@link DebitCardValidator#validatePin(String)} method
	 * 
	 */
	@Test
	public void validatePinInvalidPin4() {
		boolean result = DebitCardValidator.validatePin("1234");
		Assert.assertFalse(result);
	}

	/**
	 * This is a negative test case method to test
	 * {@link DebitCardValidator#validatePin(String)} method
	 * 
	 */
	@Test
	public void validatePinInvalidPin5() {
		boolean result = DebitCardValidator.validatePin("2345");
		Assert.assertFalse(result);
	}

	/**
	 * This is a negative test case method to test
	 * {@link DebitCardValidator#validatePin(String)} method
	 * 
	 */
	@Test
	public void validatePinInvalidPin6() {
		boolean result = DebitCardValidator.validatePin("3456");
		Assert.assertFalse(result);
	}

	/**
	 * This is a negative test case method to test
	 * {@link DebitCardValidator#validatePin(String)} method
	 * 
	 */
	@Test
	public void validatePinInvalidPin7() {
		boolean result = DebitCardValidator.validatePin("4567");
		Assert.assertFalse(result);
	}

	/**
	 * This is a negative test case method to test
	 * {@link DebitCardValidator#validatePin(String)} method
	 * 
	 */
	@Test
	public void validatePinInvalidPin8() {
		boolean result = DebitCardValidator.validatePin("5678");
		Assert.assertFalse(result);
	}

	/**
	 * This is a negative test case method to test
	 * {@link DebitCardValidator#validatePin(String)} method
	 * 
	 */
	@Test
	public void validatePinInvalidPin9() {
		boolean result = DebitCardValidator.validatePin("6789");
		Assert.assertFalse(result);
	}

	/**
	 * This is a negative test case method to test
	 * {@link DebitCardValidator#validatePin(String)} method
	 * 
	 */
	@Test
	public void validatePinInvalidPin10() {
		boolean result = DebitCardValidator.validatePin("7890");
		Assert.assertFalse(result);
	}

	/**
	 * This is a negative test case method to test
	 * {@link DebitCardValidator#validatePin(String)} method
	 * 
	 */
	@Test
	public void validatePinInvalidPin11() {
		boolean result = DebitCardValidator.validatePin("0123");
		Assert.assertFalse(result);
	}

	/**
	 * This is a negative test case method to test
	 * {@link DebitCardValidator#validatePin(String)} method
	 * 
	 */
	@Test
	public void validatePinValidPin() {
		boolean result = DebitCardValidator.validatePin("5201");
		Assert.assertTrue(result);
	}
}
