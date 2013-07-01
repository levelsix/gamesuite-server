package com.lvl6.gamesuite.common.services.user;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.lvl6.gamesuite.common.dao.UserDao;
import com.lvl6.gamesuite.common.po.AuthorizedDevice;
import com.lvl6.gamesuite.common.po.User;
import com.lvl6.gamesuite.common.services.authorizeddevice.AuthorizedDeviceService;
import com.lvl6.gamesuite.user.utils.PasswordUtil;

@Component
public class LoginServiceImpl implements LoginService {
  
    private static final Logger log = LoggerFactory.getLogger(LoginServiceImpl.class);
  
  @Autowired
  protected PasswordUtil passwordUtil;

  @Autowired
  protected UserDao userDao; 
  
  @Autowired
  protected AuthorizedDeviceService authorizedDeviceService;
  

  @Override
  public AuthorizedDevice updateUserLastLogin(User inDb, DateTime now, String udid, String deviceId) {
    inDb.setLastLogin(now.toDate());
    userDao.save(inDb);
    
    //return login token inside of the authorized device
    String userId = inDb.getId();
    Date nowDate = now.toDate();
    AuthorizedDevice ad = getAuthorizedDeviceService().checkForExistingAuthorizedDevice(userId, udid);
    if (null == ad) {
      ad = getAuthorizedDeviceService().registerNewAuthorizedDevice(userId, udid, deviceId, nowDate);
    }
    getAuthorizedDeviceService().updateExpirationForAuthorizedDevice(ad, nowDate);
    return ad;
  }
  
  @Override
  public boolean validCredentials(User inDb, String nameStrangersSee, String email, String password) {
    boolean success = false;
    try {
      byte[] salt = getSalt(inDb.getSignupDate());
      String encodedPassword = passwordUtil.encodePassword(password, salt);
      boolean isPasswordValid = passwordUtil.isPasswordValid(encodedPassword, password, salt);
      boolean nameMatches = inDb.getNameStrangersSee().equals(nameStrangersSee);
      boolean emailMatches = inDb.getEmail().equals(email);
      
      log.info("nameMatches=" + nameMatches + "\t emailMatches=" + emailMatches);
      log.info("userInDb=" + inDb + "\t nameStrangersSee=" + nameStrangersSee +
	      "\t email=" + email);
      
      if(isPasswordValid && inDb.getNameStrangersSee().equals(nameStrangersSee) 
          && inDb.getEmail().equals(email)) {
        success = true; 
      }else {
        success = false;
      }
    } catch (Exception e) {
      success = false;
    }
    return success;
  }
  
  protected byte[] getSalt(Date signupDate) {
    String str =""+signupDate.getTime();
    return str.getBytes();
  }
  
  @Override
  public List<User> getFacebookUsers(List<String> facebookIds) {
    return userDao.findByFacebookIdIn(facebookIds);
  }

  @Override
  public User getUserById(String userId) {
    return userDao.findById(userId);
  }
  
  @Override
  public Map<String, User> getUserIdsToUsers(Set<String> userIds) {
      Map<String, User> idsToUsers = new HashMap<String, User>();
      List<User> users = userDao.findByIdIn(userIds);
      for (User u : users) {
	  String id = u.getId();
	  idsToUsers.put(id, u);
      }
      return idsToUsers;
  }
  
  @Override
  public void updateUserLastLogout(User inDb, DateTime now) {
      inDb.setLastLogout(now.toDate());
      userDao.save(inDb);
  }
  
  
  
  @Override
  public PasswordUtil getPasswordUtil() {
    return passwordUtil;
  }


  @Override
  public void setPasswordUtil(PasswordUtil passwordUtil) {
    this.passwordUtil = passwordUtil;
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
  public AuthorizedDeviceService getAuthorizedDeviceService() {
    return authorizedDeviceService;
  }

  @Override
  public void setAuthorizedDeviceService(
      AuthorizedDeviceService authorizedDeviceService) {
    this.authorizedDeviceService = authorizedDeviceService;
  }

}
