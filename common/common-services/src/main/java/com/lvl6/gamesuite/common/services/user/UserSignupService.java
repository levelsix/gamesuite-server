package com.lvl6.gamesuite.common.services.user;

import java.util.List;

import com.lvl6.gamesuite.common.dao.UserDao;
import com.lvl6.gamesuite.common.po.User;
import com.lvl6.gamesuite.user.utils.PasswordUtil;

public interface UserSignupService {

	public abstract User signup(String userName, String email, String password, String facebookId);

	public abstract List<User> checkForExistingUser(String facebookId, String name, String email, String udid);

	public abstract UserDao getUserDao();

	public abstract void setUserDao(UserDao userDao);

	public abstract PasswordUtil getPasswordUtil();

	public abstract void setPasswordUtil(PasswordUtil passwordUtil);

}