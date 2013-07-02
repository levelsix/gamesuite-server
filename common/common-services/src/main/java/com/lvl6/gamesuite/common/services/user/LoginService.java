package com.lvl6.gamesuite.common.services.user;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.joda.time.DateTime;

import com.lvl6.gamesuite.common.dao.UserDao;
import com.lvl6.gamesuite.common.po.AuthorizedDevice;
import com.lvl6.gamesuite.common.po.User;
import com.lvl6.gamesuite.common.services.authorizeddevice.AuthorizedDeviceService;
import com.lvl6.gamesuite.user.utils.PasswordUtil;

public interface LoginService {

    /**
     * Return the login token (via an AuthorizedDevice object) so the user doesn't have to
     * keep providing personal information.
     */
    public abstract AuthorizedDevice updateUserLastLogin(List<User> inDbList, DateTime now, String udid, String deviceId);

    public abstract boolean validCredentials(User inDb, String nameStrangersSee, String email, String password);

    public abstract List<User> getFacebookUsers(List<String> facebookIds);

    public abstract User getUserById(String userId);

    public abstract Map<String, User> getUserIdsToUsers(Set<String> userIds);

    public abstract User updateUserLastLogout(User inDb, DateTime now);

    public abstract PasswordUtil getPasswordUtil();

    public abstract void setPasswordUtil(PasswordUtil passwordUtil);

    public abstract UserDao getUserDao();

    public abstract void setUserDao(UserDao userDao);

    public AuthorizedDeviceService getAuthorizedDeviceService();

    public void setAuthorizedDeviceService(AuthorizedDeviceService authorizedDeviceService);

}