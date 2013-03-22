package com.lvl6.common.persistence.test;

import java.util.Date;

import javax.annotation.Resource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.lvl6.gamesuite.common.dao.UserDao;
import com.lvl6.gamesuite.common.po.User;




@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("/spring-db.xml")
public class TestJpa {
	
	Logger log = LoggerFactory.getLogger(getClass());
	
	@Resource
	protected UserDao userDao;
	
	
	
	@Test
	public void testSetup() {
		log.info("Testing JPA setup");
		User testuser = new User();
		testuser.setName("Test User");
		testuser.setUdid("TestUser1");
		testuser.setLastLogin(new Date());
		User fromDb = getUserDao().findByUdid(testuser.getUdid());
		if(fromDb != null) {
			fromDb.setLastLogin(new Date());
			getUserDao().save(fromDb);
		}else {
			getUserDao().save(testuser);
		}
	}



	public UserDao getUserDao() {
		return userDao;
	}



	public void setUserDao(UserDao userDao) {
		this.userDao = userDao;
	}
}
