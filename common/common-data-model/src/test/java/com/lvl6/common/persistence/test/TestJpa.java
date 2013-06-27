package com.lvl6.common.persistence.test;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.joda.time.DateTime;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

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
	
	
	@Transactional
	@Rollback(true)
  @Test
	public void testSetup() {
		log.info("Testing JPA setup");
		User testuser = new User();
		testuser.setNameStrangersSee("Test User");
		testuser.setNameFriendsSee("hihhj");
		testuser.setEmail("foo@gmail.com");
		
		//testuser.setUdid("TestUser1");
		testuser.setLastLogin(new Date());
		assertNull("testuser=" + testuser, testuser.getId());
		List<User> existing = getUserDao().findByNameStrangersSee(testuser.getNameStrangersSee());
		assertTrue("searched for name strangers see: " + 
		    testuser.getNameStrangersSee() + ", got:" + existing, existing.isEmpty());
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

	@Transactional
	@Rollback(true)
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
    DateTime expiry = new DateTime(ad.getCreated().getTime());
    
    int days = PoConstants.AUTHORIZED_DEVICE__TOKEN_LIFE_EXPECTANCY_DAYS;
    DateTime expiry2 = expiry.plusDays(days);
    
    ad.setExpires(expiry2.toDate());
    ad = getAuthorizedDeviceDao().save(ad);
    
    assertNotNull("AuthorizedDevice was just set, though...", ad);
    assertNotNull("No expiry date", ad.getExpires());
    assertNotNull("No create date", ad.getCreated());
    assertNotNull("No login token", ad.getToken());
    
    long thirtyDayMillis = new Long(days) * 24L * 60L * 60L * 1000L;
    
    Date expiration = ad.getExpires();
    Date created = ad.getCreated();
    
    //GET DATE AUTHORIZED TOKEN CREATED ADD 30 DAYS AND SEE IF IT MATCHES
    //EXPIRATION DATE
    DateTime begin = new DateTime(created.getTime());
    DateTime expectedEnd = new DateTime(begin.toDate().getTime() + thirtyDayMillis);
    log.info("expectedEnd=" + expectedEnd + "\t millis=" + expectedEnd.toDate().getTime());
    log.info("thirtyDayMillis=" + thirtyDayMillis);
    
    Date expectedEndDate = expectedEnd.toDate();
    assertTrue("saving expiration time is off. Expected:" + expectedEndDate +
	    " millis=" + expectedEndDate.getTime() + "\t Actual:" +
	    expiration + " millis=" + expiration.getTime(),
	    expectedEndDate.getTime() == expiration.getTime());
    
    
    //GET DATE AUTHORIZED TOKEN CREATED ADD 30 DAYS (via DateTime function)
    //AND SEE IF IT MATCHES EXPIRATION DATE 
    DateTime expectedEndTwo = begin.plusDays(days);
    log.info("expectedEndTwo=" + expectedEndTwo + "\t millis=" + expectedEndTwo.toDate().getTime());
    
    Date expectedEndDateTwo = expectedEndTwo.toDate();
    assertTrue("saving expiration time is off. Expected:" + expectedEndDateTwo +
	    " millis=" + expectedEndDateTwo.getTime() + "\t Actual:" +
	    expiration + " millis=" + expiration.getTime(),
	    expectedEndDateTwo.getTime() == expiration.getTime());
    
  }

	@Transactional
  @Rollback(true)
  @Test
  public void testDuplicateNameOrEmail() {
    User joeGmail = new User();
    joeGmail.setNameStrangersSee("joe");
    joeGmail.setNameFriendsSee("foo");
    joeGmail.setEmail("joe@gmail.com");
    joeGmail.setPassword("123");
    
    getUserDao().save(joeGmail);
    
    //same nameStrangersSee as above
    String joe = "Joe";
    String josephGmail = "joe2@gmail.com";
    
    //same email as above
    String josef = "josef";
    String josefGmail = "Joe@gmail.com";
    
    List<User> existing = getUserDao().findByEmailOrNameStrangersSeeAndPasswordIsNotNull(josephGmail, joe);
    assertNotNull("searched for email=" + josephGmail + ", name=" + joe, existing);
    assertTrue("existing=" + existing, !existing.isEmpty());
    existing = getUserDao().findByEmailOrNameStrangersSeeAndPasswordIsNotNull(josefGmail, josef);
    assertNotNull("searched for email=" + josefGmail + ", name=" + josef, existing);
    assertTrue("existing=" + existing, !existing.isEmpty());
  }
  
	@Transactional
  @Rollback(true)
  @Test
  public void testGetAuthorizedDevicesExceptSome() {
    String userId = "userId";
    String udid = "udid";
    String deviceId = "deviceId";
    
    
    String udid2 = "udid2";
    String deviceId2 = "deviceId2";
    
    DateTime expiry = new DateTime();
    expiry = expiry.plusDays(PoConstants.AUTHORIZED_DEVICE__TOKEN_LIFE_EXPECTANCY_DAYS);
    
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
    getAuthorizedDeviceDao().save(ad);
    getAuthorizedDeviceDao().save(adList);
    
    long deviceCount = getAuthorizedDeviceDao().count();
    Assert.assertTrue(deviceCount > 0);
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
