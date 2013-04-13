package com.lvl6.gamesuite.common.services.user;

import com.lvl6.gamesuite.common.po.AuthorizedDevice;

public interface LoginService {

	public abstract AuthorizedDevice login(String userEmail, String password, String udid);

}