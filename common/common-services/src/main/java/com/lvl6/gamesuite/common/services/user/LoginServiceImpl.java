package com.lvl6.gamesuite.common.services.user;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;

import com.lvl6.gamesuite.common.po.User;
import com.lvl6.gamesuite.user.utils.PasswordUtil;

public class LoginServiceImpl implements LoginService {
  
  
  @Autowired
  protected PasswordUtil passwordUtil;

  @Override
  public boolean validCredentials(User inDb, String nameStrangersSee, String email, String password) {
    String encodedPassword = passwordUtil.encodePassword(password, getSalt(inDb.getSignupDate()));
    if(encodedPassword.equals(inDb.getPassword()) && inDb.getNameStrangersSee().equals(nameStrangersSee) 
        && inDb.getEmail().equals(email)) {
      return true; 
    }else {
      return false;
    }
  }

  protected byte[] getSalt(Date signupDate) {
    String str =""+signupDate.getTime();
    return str.getBytes();
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
