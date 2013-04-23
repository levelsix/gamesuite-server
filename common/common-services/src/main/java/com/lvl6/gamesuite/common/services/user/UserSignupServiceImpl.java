package com.lvl6.gamesuite.common.services.user;


import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.lvl6.gamesuite.common.dao.UserDao;
import com.lvl6.gamesuite.common.po.User;
import com.lvl6.gamesuite.user.utils.PasswordUtil;

public class UserSignupServiceImpl implements UserSignupService {
	
	private static final Logger log = LoggerFactory.getLogger(UserSignupServiceImpl.class);
	
	
	@Autowired
	protected UserDao userDao; 
	
	@Autowired
	protected PasswordUtil passwordUtil;
	


	@Override
	@Transactional
	public User signup(String userName, String email, String password, String facebookId) {
	  //decided to make this more fine-grained, (i.e. less things done) so this function 
	  //is only called to actually sign up a user, checking should be done by caller
		/*User user = checkForExistingUser(userName, email, facebookId);
		if(user != null) {
			log.warn("User {}--{} already exists..checking credentials", userName, email);
			String encodedPassword = passwordUtil.encodePassword(password, getSalt(user.getSignupDate()));
			if(encodedPassword.equals(user.getPassword()) && user.getName().equals(userName) && user.getEmail().equals(email)) {
			//check credentials
				return user; 
			}else {
				throw new RuntimeException("Username or email already exists");
			}
		}else { }*/
	  User newUser = new User();
	  newUser.setName(userName);
	  if (null != email) {
	    newUser.setEmail(email);
	  }
	  if (null != facebookId) {
	    newUser.setFacebookId(facebookId);
	  }
	  if (null != password) {
	    newUser.setPassword(PasswordUtil.encryptPassword(password, getSalt(newUser.getSignupDate())));
	  }
	  log.debug("Saving new user. user=" + newUser);
	  newUser = userDao.save(newUser);
	  return newUser;
	}
	
	protected byte[] getSalt(Date signupDate) {
		String str =""+signupDate.getTime();
		return str.getBytes();
	}
	
	@Override
	public List<User> checkForExistingUser(String facebookId, String name, String email, String udid) {
	  List<User> existing = null;
	  if (null != facebookId && null != email) {
	    existing = userDao.findByFacebookIdOrEmail(facebookId, email);
	    
	  } else if (null != facebookId) {
	    existing = userDao.findByFacebookId(facebookId);
	    
	  } else if (null != name && null != email) {
		  //only for users who create an account via (name, email, password), email must be unique and 
		  //the name must be unique among other fellow users who created an account via (name, email, password)  
		  existing = userDao.findByEmailOrNameAndPasswordIsNotNull(email, name);
		  
		} else if (null != email) {
		  existing = userDao.findByEmail(email);
		}
		//TODO: FIND BY UDID, 
		//if more than one, get the most recent person
		return existing;
	}

	
	@Override
	public UserDao getUserDao() {
		return userDao;
	}


	@Override
	public void setUserDao(UserDao userDao) {
		this.userDao = userDao;
	}


	@Override
	public PasswordUtil getPasswordUtil() {
		return passwordUtil;
	}


	@Override
	public void setPasswordUtil(PasswordUtil passwordUtil) {
		this.passwordUtil = passwordUtil;
	}
	
	
}
