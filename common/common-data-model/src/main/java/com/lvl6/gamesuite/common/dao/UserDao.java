package com.lvl6.gamesuite.common.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.lvl6.gamesuite.common.po.User;

public interface UserDao extends JpaRepository<User, String> {

	public User findByName(String userName);
	
	public User findByEmail(String email);
	
	public User findByFacebookId(String facebookId);
	
}
