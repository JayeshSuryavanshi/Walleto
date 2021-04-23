package com.amigowallet.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.amigowallet.entity.UserEntity;
import com.amigowallet.model.SecurityQuestion;
import com.amigowallet.model.User;

/**
 * This is a DAO class having methods to perform CRUD operations on user reset password and user tables.
 *
 * @author ETA_JAVA
 * 
 */
@Repository(value="forgotPasswordDAO")
public class ForgotPasswordDAOImpl implements ForgotPasswordDAO{
	
	/** This is a spring auto-wired attribute used to create data base sessions */
	@Autowired
	EntityManager entityManager;
	
	/**
	 * 
	 * This method receives email id as argument and verifies it. <br>
	 * If verification is success it returns "Found" else it returns "Not Found"
	 * 
	 * @param emailId
	 * 
	 * @return String
	 */
	@Override
	@SuppressWarnings("unchecked")
	public User authenticateEmailId(String emailId) throws Exception
	{		
		Query query = entityManager.createQuery("from UserEntity where emailId=:emailId");
		query.setParameter("emailId", emailId.toLowerCase());
		List<UserEntity> userEntities = query.getResultList();
		
		/*
		 * If listOfUserEntities is not empty it will return
		 * Found and if it is empty it will return Not Found
		 */
		if(!userEntities.isEmpty()){
			UserEntity userEntity = userEntities.get(0);
			User user = new User();
			user.setUserId(userEntity.getUserId());
			if(userEntity.getSecurityQuestion()!=null){
				SecurityQuestion securityQuestion = new SecurityQuestion();
				securityQuestion.setQuestionId(userEntity.getSecurityQuestion().getQuestionId());
				securityQuestion.setQuestion(userEntity.getSecurityQuestion().getQuestion());
				user.setSecurityQuestion(securityQuestion);
			}
			return user;
		}
		else
			return null;
	}
	
	/**
	 * 
	 * This method receives email id and hashed password as arguments and
	 * updates the password of the customer to whom the passed email id belongs
	 * 
	 * @param emailId
	 * @param hashedPassword
	 */
	@Override
	public void resetPassword(Integer userId, String hashedPassword) {
		
		 /* uniqueResult: is used to execute the query and get 
		  * single entity instead of list of entities
		  */
		UserEntity userEntity = entityManager.find(UserEntity.class, userId);	
		
		userEntity.setPassword(hashedPassword);
	}
	
	@Override
	public User validateSecurityAnswer(User user) {
		
		UserEntity userEntity = entityManager.find(UserEntity.class, user.getUserId());
		
		/*
		 * If listOfUserEntities is not empty it will return
		 * Found and if it is empty it will return Not Found
		 */
		if(userEntity!=null){
			User userFromDB = new User();
			userFromDB.setUserId(userEntity.getUserId());
			userFromDB.setSecurityAnswer(userEntity.getSecurityAnswer());
			return userFromDB;
		}
		else {
			return null;
		}
	}
}
