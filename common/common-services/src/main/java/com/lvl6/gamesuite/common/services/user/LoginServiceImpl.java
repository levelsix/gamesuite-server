package com.lvl6.gamesuite.common.services.user;

import com.lvl6.gamesuite.common.po.AuthorizedDevice;

public class LoginServiceImpl implements LoginService {
	

	@Override
	public AuthorizedDevice login(String userEmail, String password, String udid) {
		//TODO: make this work
		return new AuthorizedDevice();
	}
}
