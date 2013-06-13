package com.lvl6.gamesuite.common.services.user;


import java.util.Date;
import java.util.List;
import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.lvl6.gamesuite.common.dao.UserDao;
import com.lvl6.gamesuite.common.po.User;
import com.lvl6.gamesuite.common.properties.PoConstants;
import com.lvl6.gamesuite.user.utils.PasswordUtil;

@Component
public class UserSignupServiceImpl implements UserSignupService {
	
	private static final Logger log = LoggerFactory.getLogger(UserSignupServiceImpl.class);
	
	
	@Autowired
	protected UserDao userDao; 
	
//	@Autowired
//	protected PasswordUtil passwordUtil;
	
  protected Random rand = new Random();
	


  @Override
	@Transactional
	public User signup(String nameStrangersSee, String nameFriendsSee,
	    String email, String password, String facebookId) {
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
	  newUser.setNameStrangersSee(nameStrangersSee);
	  if (null != nameFriendsSee) {
	    newUser.setNameFriendsSee(nameFriendsSee);
	  }
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
	public List<User> checkForExistingUser(String facebookId, String nameStrangersSee, String email, String udid) {
	  List<User> existing = null;
	  if (null != facebookId && null != email) {
	    existing = userDao.findByFacebookIdOrEmail(facebookId, email);

	  } else if (null != facebookId) {
	    existing = userDao.findByFacebookId(facebookId);

	  } else if (null != nameStrangersSee && null != email) {
	    //only for users who create an account via (nameStrangersSee, email, password), email must be unique and 
	    //the nameStrangersSee must be unique among other fellow users who created an account via (nameStrangersSee, email, password)  
	    existing = userDao.findByEmailOrNameStrangersSeeAndPasswordIsNotNull(email, nameStrangersSee);

	  } else if (null!= nameStrangersSee) {
	    existing = userDao.findByNameStrangersSee(nameStrangersSee);

	  } else if (null != email) {
	    existing = userDao.findByEmail(email);
	  }
	  //TODO: FIND BY UDID (?) 
	  
	  return existing;
	}

  
  public String generateRandomName(String name) {
    String returnName = null;
    
    String facebookIdNull = null;
    String emailNull = null;
    String udidNull = null;
    if (null == name || name.isEmpty()) {
      name = PoConstants.USER__DEFAULT_NAME_STRANGERS_SEE;
    }
    int limit = PoConstants.USER__DEFAULT_ATTEMPTS_TO_GENERATE_RANDOM_NAME;
    for (int i = 0; i < limit; i++) {
      long randNum = rand.nextLong();
      StringBuffer nameAndNum = new StringBuffer();
      nameAndNum.append(name);
      nameAndNum.append(randNum);
      List<User> existing = checkForExistingUser(facebookIdNull,
          nameAndNum.toString(), emailNull, udidNull);

      if (null == existing || existing.isEmpty()) {
        returnName = nameAndNum.toString();
        break;
      } 
    }
    if (null != returnName) {
      int indexOfLastChar = PoConstants.USER__MAX_NAME_STRANGERS_SEE_LENGTH;
      indexOfLastChar = Math.min(indexOfLastChar, returnName.length());
      return returnName.substring(0, indexOfLastChar);
    }
    return returnName;
  }
  
  @Override
  public User getRandomUser() {
    int numUsers = (int) userDao.count();
    int pageNumber = rand.nextInt(numUsers);
    int size = 1;
    
    Page<User> result = userDao.findAll(new PageRequest(pageNumber, size));
    //TODO: maybe some error checking
    return result.getContent().get(0);
  }
  
  

  public Random getRand() {
    return rand;
  }

  public void setRand(Random rand) {
    this.rand = rand;
  }
	
	@Override
	public UserDao getUserDao() {
		return userDao;
	}


	@Override
	public void setUserDao(UserDao userDao) {
		this.userDao = userDao;
	}


//	@Override
//	public PasswordUtil getPasswordUtil() {
//		return passwordUtil;
//	}
//
//
//	@Override
//	public void setPasswordUtil(PasswordUtil passwordUtil) {
//		this.passwordUtil = passwordUtil;
//	}
	
	
}
