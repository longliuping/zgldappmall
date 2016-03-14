package com.zgld.mall.beans;

import java.sql.Timestamp;
import java.util.Set;

/**
 * AspnetUsers entity. @author MyEclipse Persistence Tools
 */
public class AspnetUsers extends AbstractAspnetUsers implements
		java.io.Serializable {

	// Constructors

	/** default constructor */
	public AspnetUsers() {
	}

	/** minimal constructor */
	public AspnetUsers(String userName, String loweredUserName,
			Boolean isAnonymous, Timestamp lastActivityDate, String password,
			Integer passwordFormat, String passwordSalt, Boolean isApproved,
			Boolean isLockedOut, Timestamp createDate, Timestamp lastLoginDate,
			Timestamp lastPasswordChangedDate, Timestamp lastLockoutDate,
			Integer failedPasswordAttemptCount,
			Timestamp failedPasswordAttemptWindowStart,
			Integer failedPasswordAnswerAttemptCount,
			Timestamp failedPasswordAnswerAttemptWindowStart, String sessionId) {
		super(userName, loweredUserName, isAnonymous, lastActivityDate,
				password, passwordFormat, passwordSalt, isApproved,
				isLockedOut, createDate, lastLoginDate,
				lastPasswordChangedDate, lastLockoutDate,
				failedPasswordAttemptCount, failedPasswordAttemptWindowStart,
				failedPasswordAnswerAttemptCount,
				failedPasswordAnswerAttemptWindowStart, sessionId);
	}

	/** full constructor */
	public AspnetUsers(String userName, String loweredUserName,
			String mobilePin, Boolean isAnonymous, Timestamp lastActivityDate,
			String password, Integer passwordFormat, String passwordSalt,
			String email, String loweredEmail, String passwordQuestion,
			String passwordAnswer, Boolean isApproved, Boolean isLockedOut,
			Timestamp createDate, Timestamp lastLoginDate,
			Timestamp lastPasswordChangedDate, Timestamp lastLockoutDate,
			Integer failedPasswordAttemptCount,
			Timestamp failedPasswordAttemptWindowStart,
			Integer failedPasswordAnswerAttemptCount,
			Timestamp failedPasswordAnswerAttemptWindowStart, String comment,
			Integer gender, Timestamp birthDate, Integer userRole,
			String openId, String openIdType, String sessionId,
			Set aspnetUsersInRoleses) {
		super(userName, loweredUserName, mobilePin, isAnonymous,
				lastActivityDate, password, passwordFormat, passwordSalt,
				email, loweredEmail, passwordQuestion, passwordAnswer,
				isApproved, isLockedOut, createDate, lastLoginDate,
				lastPasswordChangedDate, lastLockoutDate,
				failedPasswordAttemptCount, failedPasswordAttemptWindowStart,
				failedPasswordAnswerAttemptCount,
				failedPasswordAnswerAttemptWindowStart, comment, gender,
				birthDate, userRole, openId, openIdType, sessionId,
				aspnetUsersInRoleses);
	}

}
