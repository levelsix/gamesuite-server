package com.lvl6.gamesuite.common.services.user;

import com.lvl6.gamesuite.common.dao.UserDao;
import com.lvl6.gamesuite.common.eventprotos.CreateAccountEventProto.CreateAccountResponseProto.Builder;
import com.lvl6.gamesuite.common.po.User;
import com.lvl6.gamesuite.user.utils.PasswordUtil;

public interface UserSignupService {
  
  public abstract boolean isValidRequest(Builder aBuilder);

	public abstract User signup(String userName, String email, String password, String facebookId);

	public abstract UserDao getUserDao();

	public abstract void setUserDao(UserDao userDao);

	public abstract PasswordUtil getPasswordUtil();

	public abstract void setPasswordUtil(PasswordUtil passwordUtil);

}