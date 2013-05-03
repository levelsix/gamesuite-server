package com.lvl6.common.persistence.test;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.joda.time.DateTime;
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
import com.lvl6.gamesuite.common.properties.PoConstants;




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
		testuser.setNameStrangersSee("Test User");
		testuser.setNameFriendsSee("hi");
		//testuser.setUdid("TestUser1");
		testuser.setLastLogin(new Date());
		List<User> existing = getUserDao().findByNameStrangersSee(testuser.getNameStrangersSee());
		assertTrue("searched for name strangers see: " + 
		    testuser.getNameStrangersSee(), existing.isEmpty());
		User fromDb = null;
		if(null != existing && !existing.isEmpty()) {
		  fromDb = existing.get(0);
		}
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
    DateTime expiry = new DateTime();
    expiry.plusDays(PoConstants.AUTHORIZED_DEVICE__TOKEN_LIFE_EXPECTANCY_DAYS);
    ad.setExpires(expiry.toDate());
    getAuthorizedDeviceDao().save(ad);
    
    assertNotNull("AuthorizedDevice was just set, though...", ad);
    assertNotNull("No expiry date", ad.getExpires());
    assertNotNull("No create date", ad.getCreated());
    assertNotNull("No login token", ad.getToken());
  }

  @Test
  public void testDuplicateNameOrEmail() {
    User joeGmail = new User();
    joeGmail.setNameStrangersSee("joe");
    joeGmail.setEmail("joe@gmail.com");
    joeGmail.setPassword("123");
    
    User joeFacebook = new User();
    joeFacebook.setNameStrangersSee("joe");
    joeFacebook.setFacebookId("facebookId");
    
    List<User> joeList = new ArrayList<User>();
    joeList.add(joeGmail);
    joeList.add(joeFacebook);
    getUserDao().save(joeList);
    
    String joseph = "Joe";
    String josephGmail = "joe2@gmail.com";
    
    String josef = "josef";
    String josefGmail = "Joe@gmail.com";
    
    List<User> existing = getUserDao().findByEmailOrNameStrangersSeeAndPasswordIsNotNull(josephGmail, joseph);
    assertNotNull("searched for email=" + josephGmail + ", name=" + joseph, existing);
    existing = getUserDao().findByEmailOrNameStrangersSeeAndPasswordIsNotNull(josefGmail, josef);
    assertNotNull("searched for email=" + josefGmail + ", name=" + josef, existing);
    
  }
  
  @Test
  public void testGetAuthorizedDevicesExceptSome() {
    String userId = "userId";
    String udid = "udid";
    String deviceId = "deviceId";
    
    String udid2 = "udid2";
    String deviceId2 = "deviceId2";
    
    DateTime expiry = new DateTime();
    expiry.plusDays(PoConstants.AUTHORIZED_DEVICE__TOKEN_LIFE_EXPECTANCY_DAYS);
    
    //first device for user
    AuthorizedDevice ad = new AuthorizedDevice();
    ad.setUserId(userId);
    ad.setUdid(udid);
    ad.setDeviceId(deviceId);
    ad.setExpires(expiry.toDate());
    
    //second device for user
    AuthorizedDevice ad2 = new AuthorizedDevice();
    ad2.setUserId(userId);
    ad2.setUdid(udid2);
    ad2.setDeviceId(deviceId2);
    ad2.setExpires(expiry.toDate());
    
    //save both to the database
    List<AuthorizedDevice> adList = new ArrayList<AuthorizedDevice>();
    adList.add(ad2);
    adList.add(ad);
    getAuthorizedDeviceDao().save(adList);
    
    //the authorized device ids to exclude
    List<String> id = new ArrayList<String>();
    id.add(ad.getId());
    
    //log.error("adList=" + adList);
    //log.error("id=" + id);
    
    //get all but the first device for user
    assertNotNull("wtf userId is null", userId);
    assertNotNull("wtf id is null", id);
    assertTrue("id=" + id, !id.isEmpty());
    List<AuthorizedDevice> inDbList = getAuthorizedDeviceDao().findByUserIdAndIdNotIn(userId, id);
    
    assertTrue("inDbList=" + inDbList, inDbList.size() == 1);
    log.info("Should be same objects: inDbList=" + inDbList + " ad2=" + ad2);
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
