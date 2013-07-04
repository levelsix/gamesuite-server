package com.lvl6.gamesuite.common.services.authorizeddevice;

import java.util.ArrayList;
import java.util.Date;
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


@Component
public class AuthorizedDeviceServiceImpl implements AuthorizedDeviceService {


    private static final Logger log = LoggerFactory.getLogger(AuthorizedDeviceServiceImpl.class);


    @Autowired
    protected AuthorizedDeviceDao authorizedDeviceDao;

    @Override
    @Transactional
    public AuthorizedDevice registerNewAuthorizedDevice(String userId, String udid,
	    String deviceId, Date now) {
	AuthorizedDevice ad = null;
	if (isValidUdid(udid)) {
	    ad = new AuthorizedDevice();
	    ad.setUserId(userId);
	    ad.setUdid(udid);
	    ad.setDeviceId(deviceId);
	    if (null != now) {
		ad.setCreated(now);
	    }

	    DateTime expiry = new DateTime(ad.getCreated().getTime());
	    expiry = expiry.plusDays(PoConstants.AUTHORIZED_DEVICE__TOKEN_LIFE_EXPECTANCY_DAYS);
	    ad.setExpires(expiry.toDate());

	    ad = authorizedDeviceDao.save(ad);
	}

	return ad;
    }

    public AuthorizedDevice updateExpirationForAuthorizedDevice(AuthorizedDevice ad,
	    Date fromNow) {
	if (null == ad) {
	    return null;
	}

	//now + default life expectancy of a token
	DateTime expiry = new DateTime(fromNow.getTime());
	expiry = expiry.plusDays(PoConstants.AUTHORIZED_DEVICE__TOKEN_LIFE_EXPECTANCY_DAYS);
	ad.setExpires(expiry.toDate());
	ad = authorizedDeviceDao.save(ad);
	return ad;
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
    public List<AuthorizedDevice> devicesSharingUserAccount(String userId, AuthorizedDevice exempt) {
	List<AuthorizedDevice> otherDevices = null;
	if (null != exempt) {
	    List<String> exemptions = new ArrayList<String>();
	    String exemptId = exempt.getId();
	    exemptions.add(exemptId);
	    otherDevices = getAuthorizedDeviceDao().findByUserIdAndIdNotIn(userId, exemptions);
	} else {
	    otherDevices = getAuthorizedDeviceDao().findByUserId(userId);
	}

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