package com.edubank.dao;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.stereotype.Repository;

import com.edubank.entity.SecurityQuestionEntity;
import com.edubank.model.SecurityQuestion;

/**
 * This class contains the methods responsible for interacting with the database
 * with respect to security questions functionality
 * 
 * @author ETA_JAVA
 *
 */
@Repository(value = "securityQuestionDao")
public class SecurityQuestionDAOImpl implements SecurityQuestionDAO {

	@PersistenceContext
	EntityManager entityManager;

	/**
	 * This method is used to get all the security questions
	 * 
	 * @return List<SecurityQuestion>
	 */
	@Override
	public List<SecurityQuestion> getAllSecurityQuestions() throws Exception {

		List<SecurityQuestion> securityQuestionsList = new ArrayList<>();

		Query query = entityManager.createQuery("from SecurityQuestionEntity");
		List<SecurityQuestionEntity> securityQuesitonsListFromDAO = query.getResultList();

		for (SecurityQuestionEntity questionEntity : securityQuesitonsListFromDAO) {
			SecurityQuestion securityQuestion = new SecurityQuestion();
			securityQuestion.setQuestionId(questionEntity.getQuestionId());
			securityQuestion.setQuestion(questionEntity.getQuestion());
			securityQuestionsList.add(securityQuestion);
		}

		return securityQuestionsList;
	}
}
