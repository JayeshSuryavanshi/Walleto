package com.edubank.service;

import java.util.List;

import com.edubank.model.SecurityQuestion;

/**
 * This interface contains the methods for business logics related to security questions,
 * 
 * @author ETA_JAVA
 *
 */
public interface SecurityQuestionService {
	
	/**
	 * This method is used to get all the security questions
	 * 
	 * @return List<SecurityQuestion>
	 * 
	 */
	public List<SecurityQuestion> getAllSecurityQuestions() throws Exception;
}
