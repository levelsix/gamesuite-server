package com.lvl6.gamesuite.common.services.user;


import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.lvl6.gamesuite.common.dao.UserDao;
import com.lvl6.gamesuite.common.po.User;
import com.lvl6.gamesuite.user.utils.PasswordUtil;

public class UserSignupService {
	
	private static final Logger log = LoggerFactory.getLogger(UserSignupService.class);
	
	
	@Autowired
	protected UserDao userDao; 
	
	@Autowired
	protected PasswordUtil passwordUtil;
	
	
	public User signup(String userName, String email, String password, String facebookId) {
		User user = checkForExistingUser(userName, email, facebookId);
		if(user != null) {
			log.warn("User {}--{} already exists..checking credentials", userName, email);
			String encodedPassword = passwordUtil.encodePassword(password, getSalt(user.getSignupDate()));
			if(encodedPassword.equals(user.getPassword()) && user.getName().equals(userName) && user.getEmail().equals(email)) {
			//check credentials
				return user; 
			}else {
				throw new RuntimeException("Username or email already exists");
			}
		}else {
			User newUser = new User();
			newUser.setName(userName);
			newUser.setEmail(email);
			newUser.setFacebookId(facebookId);
			newUser.setPassword(PasswordUtil.encryptPassword(password, getSalt(newUser.getSignupDate())));
			return newUser;
		}
	}
	
	protected byte[] getSalt(Date signupDate) {
		String str =""+signupDate.getTime();
		return str.getBytes();
	}
	
	protected User checkForExistingUser(String userName, String email, String facebookId) {
		User existing = userDao.findByNameOrEmail(userName, email);
		if(existing == null && facebookId != "" && facebookId != null) {
			existing = userDao.findByFacebookId(facebookId);
			return existing;
		}else {
			return existing;
		}
	}

	public UserDao getUserDao() {
		return userDao;
	}

	public void setUserDao(UserDao userDao) {
		this.userDao = userDao;
	}

	public PasswordUtil getPasswordUtil() {
		return passwordUtil;
	}

	public void setPasswordUtil(PasswordUtil passwordUtil) {
		this.passwordUtil = passwordUtil;
	}
	
	
}
