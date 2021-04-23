package com.edubank.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * This is an entity class mapped to DataBase table <q>EDUBANK_SECURITY_QUESTION</q>
 *
 * @GenericGenerator is used for auto generating primary key value
 * 
 * @author ETA_JAVA
 *
 */
@Entity
@Table(name="EDUBANK_SECURITY_QUESTION")
public class SecurityQuestionEntity {
	
	/** @Id is used to map primary key of this table
	 *
	 *
	 *  @column is used to map a property with a column in table where column name is different
	 *  in this case 'questionId' is mapped with QUESTION_ID column of this table 
	 */
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
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
