package com.edubank.model;

import java.io.Serializable;

/**
 * This is a bean class. Also called as model class has the attributes to keep security question properties
 *
 * @author ETA_JAVA
 *
 **/
@SuppressWarnings("serial")
public class SecurityQuestion implements Serializable {
	private Integer questionId;
	private String question;
	
	public Integer getQuestionId() {
		return questionId;
	}
	public void setQuestionId(Integer questionId) {
		this.questionId = questionId;
	}
	public String getQuestion() {
		return question;
	}
	public void setQuestion(String question) {
		this.question = question;
	}
}
