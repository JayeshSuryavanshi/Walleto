package com.amigowallet.entity;

import java.time.LocalDateTime;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.amigowallet.model.UserStatus;

@Entity
@Table(name = "WALLET_USER")
public class UserEntity {

	@Id
	@Column(name = "USER_ID")
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer userId;
	@Column(name = "EMAIL_ID")
	private String emailId;
	@Column(name = "MOBILE_NUMBER")
	private String mobileNumber;
	@Column(name = "NAME")
	private String name;
	@Column(name = "PASSWORD")
	private String password;

	@Enumerated(EnumType.STRING)
	@Column(name = "USER_STATUS")
	private UserStatus userStatus;

	@ManyToOne(cascade=CascadeType.ALL)
	@JoinColumn(name="SECURITY_QUESTION_ID")
	private SecurityQuestionEntity securityQuestion;
	
	@Column(name="SECURITY_ANSWER")
	private String securityAnswer;
	
	@CreationTimestamp
	@Column(name = "CREATED_TIMESTAMP")
	private LocalDateTime createdTimestamp;
	@UpdateTimestamp
	@Column(name = "MODIFIED_TIMESTAMP")
	private LocalDateTime modifiedTimestamp;

	
	@OneToMany(cascade = CascadeType.ALL)
	@JoinColumn(name = "USER_ID")
	private List<CardEntity> cardEntities;

	
	@OneToMany(cascade = CascadeType.ALL)
	@JoinColumn(name = "USER_ID")
	private List<UserTransactionEntity> userTransactionEntities;
	
	
	
	public List<CardEntity> getCardEntities() {
		return cardEntities;
	}

	public void setCardEntities(List<CardEntity> cardEntities) {
		this.cardEntities = cardEntities;
	}

	public List<UserTransactionEntity> getUserTransactionEntities() {
		return userTransactionEntities;
	}

	public void setUserTransactionEntities(
			List<UserTransactionEntity> userTransactionEntities) {
		this.userTransactionEntities = userTransactionEntities;
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public String getEmailId() {
		return emailId;
	}

	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}

	public String getMobileNumber() {
		return mobileNumber;
	}

	public void setMobileNumber(String mobileNumber) {
		this.mobileNumber = mobileNumber;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public UserStatus getUserStatus() {
		return userStatus;
	}

	public void setUserStatus(UserStatus userStatus) {
		this.userStatus = userStatus;
	}

	public LocalDateTime getCreatedTimestamp() {
		return createdTimestamp;
	}

	public void setCreatedTimestamp(LocalDateTime createdTimestamp) {
		this.createdTimestamp = createdTimestamp;
	}

	public LocalDateTime getModifiedTimestamp() {
		return modifiedTimestamp;
	}

	public void setModifiedTimestamp(LocalDateTime modifiedTimestamp) {
		this.modifiedTimestamp = modifiedTimestamp;
	}

	public SecurityQuestionEntity getSecurityQuestion() {
		return securityQuestion;
	}

	public void setSecurityQuestion(SecurityQuestionEntity securityQuestion) {
		this.securityQuestion = securityQuestion;
	}

	public String getSecurityAnswer() {
		return securityAnswer;
	}

	public void setSecurityAnswer(String securityAnswer) {
		this.securityAnswer = securityAnswer;
	}

}
