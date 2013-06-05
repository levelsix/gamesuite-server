package com.lvl6.gamesuite.common.services.authorizeddevice;

import java.util.ArrayList;
import java.util.List;

import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.lvl6.gamesuite.common.dao.AuthorizedDeviceDao;
import com.lvl6.gamesuite.common.po.AuthorizedDevice;
import com.lvl6.gamesuite.common.properties.PoConstants;
import com.lvl6.gamesuite.common.services.user.UserSignupServiceImpl;


@Component
public class AuthorizedDeviceServiceImpl implements AuthorizedDeviceService {

  private static final Logger log = LoggerFactory.getLogger(UserSignupServiceImpl.class);
  
  
  @Autowired
  protected AuthorizedDeviceDao authorizedDeviceDao;
  
  @Override
  @Transactional
  public AuthorizedDevice registerNewAuthorizedDevice(String userId, String udid,
      String deviceId) {
    AuthorizedDevice ad = null;
    if (isValidUdid(udid)) {
      ad = new AuthorizedDevice();
      ad.setUserId(userId);
      ad.setUdid(udid);
      ad.setDeviceId(deviceId);
      
      DateTime expiry = new DateTime();
      expiry = expiry.plusDays(PoConstants.AUTHORIZED_DEVICE__TOKEN_LIFE_EXPECTANCY_DAYS);
      ad.setExpires(expiry.toDate());
      
      ad = authorizedDeviceDao.save(ad);
    }
    
    return ad;
  }

  public void updateExpirationForAuthorizedDevice(AuthorizedDevice ad) {
    if (null == ad) {
      return;
    }
    
    //now + default life expectancy of a token
    DateTime expiry = new DateTime();
    expiry.plusDays(PoConstants.AUTHORIZED_DEVICE__TOKEN_LIFE_EXPECTANCY_DAYS);
    ad.setExpires(expiry.toDate());
    authorizedDeviceDao.save(ad);
  }
  
  public AuthorizedDevice checkForExistingAuthorizedDevice(String userId, String udid) {
    List<AuthorizedDevice> adList = authorizedDeviceDao.findByUserIdAndUdid(userId, udid);
    if (null != adList && adList.size() != 1) {
      log.error("unexpected error: user has more than one token for this device." +
      		" authorizedDevices=" + adList);
      return null;
    } else {
      return adList.get(0); 
    }
  }
  
  public boolean isValidUdid(String udid) {
    if (null == udid || udid.equals(PoConstants.AUTHORIZED_DEVICE__NULL_UDID)) {
      return false;
    } else {
      return true;
    }
  }
  
  @Override
  public List<AuthorizedDevice> otherDevicesSharingUserAccount(String userId, AuthorizedDevice exempt) {
    String exemptId = exempt.getId();
    List<String> exemptions = new ArrayList<String>();
    exemptions.add(exemptId);
    
    List<AuthorizedDevice> otherDevices = getAuthorizedDeviceDao().findByUserIdAndIdNotIn(userId, exemptions);
    return otherDevices;
  }
  
  
  
  @Override
  public AuthorizedDeviceDao getAuthorizedDeviceDao() {
    return authorizedDeviceDao;
  }

  @Override
  public void setAuthorizedDeviceDao(AuthorizedDeviceDao authorizedDeviceDao) {
    this.authorizedDeviceDao = authorizedDeviceDao;
  }

}