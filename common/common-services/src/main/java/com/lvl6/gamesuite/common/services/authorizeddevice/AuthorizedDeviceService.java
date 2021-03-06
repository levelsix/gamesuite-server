package com.lvl6.gamesuite.common.services.authorizeddevice;

import java.util.Date;
import java.util.List;

import com.lvl6.gamesuite.common.dao.AuthorizedDeviceDao;
import com.lvl6.gamesuite.common.po.AuthorizedDevice;

public interface AuthorizedDeviceService {

    public abstract AuthorizedDevice registerNewAuthorizedDevice(String userId, String udid, String deviceId, Date now);

    public abstract AuthorizedDevice updateExpirationForAuthorizedDevice(AuthorizedDevice ad, Date fromNow);

    public abstract AuthorizedDevice checkForExistingAuthorizedDevice(String userId, String udid);

    public abstract boolean isValidUdid(String udid);

    public abstract List<AuthorizedDevice> devicesSharingUserAccount(String userId, AuthorizedDevice exempt);

    public abstract AuthorizedDeviceDao getAuthorizedDeviceDao();

    public abstract void setAuthorizedDeviceDao(AuthorizedDeviceDao authorizedDeviceDao);

}