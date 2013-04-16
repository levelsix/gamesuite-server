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
		//testuser.setUdid("TestUser1");
		testuser.setLastLogin(new Date());
		User fromDb = getUserDao().findByName(testuser.getName());
		log.info("before: testuser=" + testuser);
		log.info("before: fromDb=" + fromDb);
		if(fromDb != null) {
			fromDb.setLastLogin(new Date());
			fromDb = getUserDao().save(fromDb);
		}else {
			testuser = getUserDao().save(testuser);
		}
		log.info("after: testuser=" + testuser);
    log.info("after: fromDb=" + fromDb);
	}



	public UserDao getUserDao() {
		return userDao;
	}



	public void setUserDao(UserDao userDao) {
		this.userDao = userDao;
	}
}
