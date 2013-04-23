package com.lvl6.gamesuite.common.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.lvl6.gamesuite.common.po.User;

public interface UserDao extends JpaRepository<User, String> {

	public List<User> findByName(String userName);
	
	public List<User> findByEmail(String email);
	
	public List<User> findByFacebookId(String facebookId);
	
	public List<User> findByFacebookIdOrEmail(String facebookId, String email);
	
	public List<User> findByEmailOrNameAndPasswordIsNotNull(String email, String name);
	
}
