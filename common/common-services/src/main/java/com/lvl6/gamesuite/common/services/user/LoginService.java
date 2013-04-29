package com.lvl6.gamesuite.common.services.user;

import com.lvl6.gamesuite.common.po.User;
import com.lvl6.gamesuite.user.utils.PasswordUtil;

public interface LoginService {

  public abstract boolean validCredentials(User inDb, String nameStrangersSee, String email, String password);

  public abstract PasswordUtil getPasswordUtil();

  public abstract void setPasswordUtil(PasswordUtil passwordUtil);
}