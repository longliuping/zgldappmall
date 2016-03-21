package com.zgld.mall.beans;

import java.sql.Timestamp;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * AbstractAspnetUsers entity provides the base persistence definition of the
 * AspnetUsers entity. @author MyEclipse Persistence Tools
 */

public abstract class AbstractAspnetUsers implements java.io.Serializable {

	// Fields

	private Integer userId;
	private String userName;
	private String loweredUserName;
	private String mobilePin;
	private Boolean isAnonymous;
	private String lastActivityDate;
	private String password;
	private Integer passwordFormat;
	private String passwordSalt;
	private String email;
	private String loweredEmail;
	private String passwordQuestion;
	private String passwordAnswer;
	private Boolean isApproved;
	private Boolean isLockedOut;
	private String createDate;
	private String lastLoginDate;
	private String lastPasswordChangedDate;
	private String lastLockoutDate;
	private Integer failedPasswordAttemptCount;
	private String failedPasswordAttemptWindowStart;
	private Integer failedPasswordAnswerAttemptCount;
	private String failedPasswordAnswerAttemptWindowStart;
	private String comment;
	private Integer gender;
	private String birthDate;
	private Integer userRole;
	private String openId;
	private String openIdType;
	private String sessionId;
	private String head;


	// Constructors

	/** default constructor */
	public AbstractAspnetUsers() {
	}

	// Property accessors

	public Integer getUserId() {
		return this.userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public String getUserName() {
		return this.userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getLoweredUserName() {
		return this.loweredUserName;
	}

	public void setLoweredUserName(String loweredUserName) {
		this.loweredUserName = loweredUserName;
	}

	public String getMobilePin() {
		return this.mobilePin;
	}

	public void setMobilePin(String mobilePin) {
		this.mobilePin = mobilePin;
	}

	public Boolean getIsAnonymous() {
		return this.isAnonymous;
	}

	public void setIsAnonymous(Boolean isAnonymous) {
		this.isAnonymous = isAnonymous;
	}

	public String getLastActivityDate() {
		return this.lastActivityDate;
	}

	public void setLastActivityDate(String lastActivityDate) {
		this.lastActivityDate = lastActivityDate;
	}

	public String getPassword() {
		return this.password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Integer getPasswordFormat() {
		return this.passwordFormat;
	}

	public void setPasswordFormat(Integer passwordFormat) {
		this.passwordFormat = passwordFormat;
	}

	public String getPasswordSalt() {
		return this.passwordSalt;
	}

	public void setPasswordSalt(String passwordSalt) {
		this.passwordSalt = passwordSalt;
	}

	public String getEmail() {
		return this.email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getLoweredEmail() {
		return this.loweredEmail;
	}

	public void setLoweredEmail(String loweredEmail) {
		this.loweredEmail = loweredEmail;
	}

	public String getPasswordQuestion() {
		return this.passwordQuestion;
	}

	public void setPasswordQuestion(String passwordQuestion) {
		this.passwordQuestion = passwordQuestion;
	}

	public String getPasswordAnswer() {
		return this.passwordAnswer;
	}

	public void setPasswordAnswer(String passwordAnswer) {
		this.passwordAnswer = passwordAnswer;
	}

	public Boolean getIsApproved() {
		return this.isApproved;
	}

	public void setIsApproved(Boolean isApproved) {
		this.isApproved = isApproved;
	}

	public Boolean getIsLockedOut() {
		return this.isLockedOut;
	}

	public void setIsLockedOut(Boolean isLockedOut) {
		this.isLockedOut = isLockedOut;
	}

	public String getCreateDate() {
		return this.createDate;
	}

	public void setCreateDate(String createDate) {
		this.createDate = createDate;
	}

	public String getLastLoginDate() {
		return this.lastLoginDate;
	}

	public void setLastLoginDate(String lastLoginDate) {
		this.lastLoginDate = lastLoginDate;
	}

	public String getLastPasswordChangedDate() {
		return this.lastPasswordChangedDate;
	}

	public void setLastPasswordChangedDate(String lastPasswordChangedDate) {
		this.lastPasswordChangedDate = lastPasswordChangedDate;
	}

	public String getLastLockoutDate() {
		return this.lastLockoutDate;
	}

	public void setLastLockoutDate(String lastLockoutDate) {
		this.lastLockoutDate = lastLockoutDate;
	}

	public Integer getFailedPasswordAttemptCount() {
		return this.failedPasswordAttemptCount;
	}

	public void setFailedPasswordAttemptCount(Integer failedPasswordAttemptCount) {
		this.failedPasswordAttemptCount = failedPasswordAttemptCount;
	}

	public String getFailedPasswordAttemptWindowStart() {
		return this.failedPasswordAttemptWindowStart;
	}

	public void setFailedPasswordAttemptWindowStart(
			String failedPasswordAttemptWindowStart) {
		this.failedPasswordAttemptWindowStart = failedPasswordAttemptWindowStart;
	}

	public Integer getFailedPasswordAnswerAttemptCount() {
		return this.failedPasswordAnswerAttemptCount;
	}

	public void setFailedPasswordAnswerAttemptCount(
			Integer failedPasswordAnswerAttemptCount) {
		this.failedPasswordAnswerAttemptCount = failedPasswordAnswerAttemptCount;
	}

	public String getFailedPasswordAnswerAttemptWindowStart() {
		return this.failedPasswordAnswerAttemptWindowStart;
	}

	public void setFailedPasswordAnswerAttemptWindowStart(
			String failedPasswordAnswerAttemptWindowStart) {
		this.failedPasswordAnswerAttemptWindowStart = failedPasswordAnswerAttemptWindowStart;
	}

	public String getComment() {
		return this.comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public Integer getGender() {
		return this.gender;
	}

	public void setGender(Integer gender) {
		this.gender = gender;
	}

	public String getBirthDate() {
		return this.birthDate;
	}

	public void setBirthDate(String birthDate) {
		this.birthDate = birthDate;
	}

	public Integer getUserRole() {
		return this.userRole;
	}

	public void setUserRole(Integer userRole) {
		this.userRole = userRole;
	}

	public String getOpenId() {
		return this.openId;
	}

	public void setOpenId(String openId) {
		this.openId = openId;
	}

	public String getOpenIdType() {
		return this.openIdType;
	}

	public void setOpenIdType(String openIdType) {
		this.openIdType = openIdType;
	}

	public String getSessionId() {
		return this.sessionId;
	}

	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}

	public String getHead() {
		return head;
	}

	public void setHead(String head) {
		this.head = head;
	}

}