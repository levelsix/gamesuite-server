package com.lvl6.common.persistence.test;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.lvl6.gamesuite.common.dao.AuthorizedDeviceDao;
import com.lvl6.gamesuite.common.dao.UserDao;
import com.lvl6.gamesuite.common.po.AuthorizedDevice;
import com.lvl6.gamesuite.common.po.User;




@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("/spring-db.xml")
public class TestJpa {
	
	Logger log = LoggerFactory.getLogger(getClass());
	
	@Resource
	protected UserDao userDao;
	
	@Resource 
	protected AuthorizedDeviceDao authorizedDeviceDao;
	
	

  @Test
	public void testSetup() {
		log.info("Testing JPA setup");
		User testuser = new User();
		testuser.setName("Test User");
		//testuser.setUdid("TestUser1");
		testuser.setLastLogin(new Date());
		List<User> existing = getUserDao().findByName(testuser.getName());
		assertTrue("searched for user name " + testuser.getName(), existing.isEmpty());
		User fromDb = (null == existing) ? null : existing.get(0);
		if(fromDb != null) {
			fromDb.setLastLogin(new Date());
			fromDb = getUserDao().save(fromDb);
		}else {
			testuser = getUserDao().save(testuser);
		}
	}

  @Test
  public void testAuthorizedDeviceCreation() {
    String userId = "userId";
    String udid = "udid";
    String deviceId = "deviceId";
    
    AuthorizedDevice ad = new AuthorizedDevice();
    log.info("not fully initialized authorizedDevice=" + ad);
    ad.setUserId(userId);
    ad.setUdid(udid);
    ad.setDeviceId(deviceId);
    getAuthorizedDeviceDao().save(ad);
    
    assertNotNull("AuthorizedDevice was just set, though...", ad);
    assertNotNull("No expiry date", ad.getExpires());
    assertNotNull("No create date", ad.getCreated());
    assertNotNull("No login token", ad.getToken());
  }

  @Test
  public void testDuplicateNameOrEmail() {
    User joeGmail = new User();
    joeGmail.setName("joe");
    joeGmail.setEmail("joe@gmail.com");
    joeGmail.setPassword("123");
    
    User joeFacebook = new User();
    joeFacebook.setName("joe");
    joeFacebook.setFacebookId("facebookId");
    
    List<User> joeList = new ArrayList<User>();
    joeList.add(joeGmail);
    joeList.add(joeFacebook);
    getUserDao().save(joeList);
    
    String joseph = "Joe";
    String josephGmail = "joe2@gmail.com";
    
    String josef = "josef";
    String josefGmail = "Joe@gmail.com";
    
    List<User> existing = getUserDao().findByEmailOrNameAndPasswordIsNotNull(josephGmail, joseph);
    assertNotNull("searched for email=" + josephGmail + ", name=" + joseph, existing);
    existing = getUserDao().findByEmailOrNameAndPasswordIsNotNull(josefGmail, josef);
    assertNotNull("searched for email=" + josefGmail + ", name=" + josef, existing);
    
  }

	public UserDao getUserDao() {
		return userDao;
	}



	public void setUserDao(UserDao userDao) {
		this.userDao = userDao;
	}
	
  
	
  public AuthorizedDeviceDao getAuthorizedDeviceDao() {
    return authorizedDeviceDao;
  }

  public void setAuthorizedDeviceDao(AuthorizedDeviceDao authorizedDeviceDao) {
    this.authorizedDeviceDao = authorizedDeviceDao;
  }
	

}
