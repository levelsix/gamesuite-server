package com.lvl6.gamesuite.common.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.lvl6.gamesuite.common.po.User;

public interface UserDao extends JpaRepository<User, String> {

	public User findByUdid(String udid);
	
	public User findByName(String userName);
	
}
