package com.edubank.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.edubank.dao.SecurityQuestionDAO;
import com.edubank.model.SecurityQuestion;

/**
 * This Class contains the methods for business logics related to security questions, like
 * getCustomer, registerCustomer
 * 
 * @author ETA_JAVA
 *
 */
@Service(value="securityQuestionService")
@Transactional
public class SecurityQuestionServiceImpl implements SecurityQuestionService {

	@Autowired
	private SecurityQuestionDAO securityQuestionDao;
	
	/**
	 * This method is used to get all the security questions
	 * 
	 * @return List<SecurityQuestion>
	 * 
	 */
	@Override
	public List<SecurityQuestion> getAllSecurityQuestions() throws Exception {
		return securityQuestionDao.getAllSecurityQuestions();
	}
}
