package com.amigowallet.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="AW_SECURITY_QUESTION")
public class SecurityQuestionEntity {
	
	@Id
	@Column(name="QUESTION_ID")
	private Integer questionId;
	@Column(name="QUESTION")
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
