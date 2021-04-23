package com.edubank.dao;

import java.util.List;

import com.edubank.model.SecurityQuestion;

/**
 * This Interface contains the methods responsible for interacting with the database with
 * respect to security questions functionality
 * 
 * @author ETA_JAVA
 *
 */
public interface SecurityQuestionDAO {
	
	/**
	 * This method is used to get all the sequrity questions
	 * 
	 * @return List<SecurityQuestion>
	 */
	public List<SecurityQuestion> getAllSecurityQuestions() throws Exception;
}
