package com.lvl6.pictures.controller;

import java.util.List;

import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.lvl6.gamesuite.common.controller.EventController;
import com.lvl6.gamesuite.common.events.RequestEvent;
import com.lvl6.pictures.eventprotos.LoginEventProto.LoginRequestProto;
import com.lvl6.pictures.eventprotos.LoginEventProto.LoginRequestProto.LoginType;
import com.lvl6.pictures.eventprotos.LoginEventProto.LoginResponseProto;
import com.lvl6.pictures.eventprotos.LoginEventProto.LoginResponseProto.Builder;
import com.lvl6.pictures.eventprotos.LoginEventProto.LoginResponseProto.LoginResponseStatus;
import com.lvl6.pictures.events.request.LoginRequestEvent;
import com.lvl6.pictures.events.response.LoginResponseEvent;
import com.lvl6.pictures.noneventprotos.PicturesEventProtocolProto.PicturesEventProtocolRequest;
import com.lvl6.pictures.noneventprotos.UserProto.BasicAuthorizedDeviceProto;
import com.lvl6.pictures.noneventprotos.UserProto.BasicUserProto;
import com.lvl6.gamesuite.common.noneventprotos.CommonEventProtocolProto.CommonEventProtocolRequest;
import com.lvl6.gamesuite.common.po.AuthorizedDevice;
import com.lvl6.gamesuite.common.po.User;
import com.lvl6.gamesuite.common.services.authorizeddevice.AuthorizedDeviceService;
import com.lvl6.gamesuite.common.services.user.LoginService;
import com.lvl6.gamesuite.common.services.user.UserSignupService;
import com.lvl6.gamesuite.user.utils.EmailUtil;

public class LoginController extends EventController {

  private static Logger log = LoggerFactory.getLogger(new Object() { }.getClass().getEnclosingClass());
  
  @Autowired
  protected UserSignupService userSignupService;
  
  @Autowired
  protected AuthorizedDeviceService authorizedDeviceService;
  
  @Autowired
  protected LoginService loginService;
  

  @Override
  public RequestEvent createRequestEvent() {
    return new LoginRequestEvent();
  }

  @Override
  public int getEventType() {
    return CommonEventProtocolRequest.C_LOGIN_EVENT_VALUE;
  }

  @Override
  protected void processRequestEvent(RequestEvent event) throws Exception {
    LoginRequestProto reqProto = ((LoginRequestEvent) event).getLoginRequestProto();
    BasicUserProto sender = reqProto.getSender();
    LoginType lt = reqProto.getLoginType();
    List<String> facebookFriendIds = reqProto.getFacebookFriendIdsList();
    boolean initializeAccount = reqProto.getInitializeAccount();
    DateTime now = new DateTime();
    
    //response to send back to client
    LoginResponseProto.Builder responseBuilder = LoginResponseProto.newBuilder();
    responseBuilder.setStatus(LoginResponseStatus.FAIL_OTHER);
    
    boolean validRequestArgs = isValidRequestArguments(responseBuilder, sender,
        lt, now); 
    boolean validRequest = false;
    boolean successful = false;
    
    if (validRequestArgs) {
      validRequest = isValidRequest(responseBuilder, sender, lt, now);
    }
    
    if (validRequest) {
      successful = writeChangesToDb(responseBuilder, sender, lt, facebookFriendIds,
          initializeAccount, now);
    }
    
    if (successful) {
      if (LoginType.LOGIN_TOKEN == lt) {
        responseBuilder.setStatus(LoginResponseStatus.SUCCESS_LOGIN_TOKEN);
      }
      if (LoginType.EMAIL_PASSWORD == lt) {
        responseBuilder.setStatus(LoginResponseStatus.SUCCESS_EMAIL_PASSWORD);
      }
      if (LoginType.FACEBOOK == lt) {
        responseBuilder.setStatus(LoginResponseStatus.SUCCESS_FACEBOOK_ID);
      }
      if (LoginType.NO_CREDENTIALS == lt) {
        responseBuilder.setStatus(LoginResponseStatus.SUCCESS_NO_CREDENTIALS);
      }
    }
    //this is what the login request event does...
    String udid = reqProto.getSender().getBadp().getUdid();
    
    LoginResponseProto resProto = responseBuilder.build();
    LoginResponseEvent resEvent = new LoginResponseEvent(udid);
    resEvent.setTag(event.getTag());
    resEvent.setLoginResponseProto(resProto);
    
    log.info("Writing event: " + resEvent);
    getEventWriter().processPreDBResponseEvent(resEvent, udid);
  }
  
  private boolean isValidRequestArguments(Builder responseBuilder, 
      BasicUserProto sender, LoginType lt, DateTime now) {
    if (null == lt) {
      log.error("unexpected error: login type is null. sender=" + sender
          + ", loginType=" + lt);
      return false;
    }
    
    BasicAuthorizedDeviceProto badp = sender.getBadp();
    if (LoginType.LOGIN_TOKEN == lt) {
      String loginToken = badp.getLoginToken();
      DateTime expiry = new DateTime(badp.getExpirationDate());
      String userId = sender.getUserId();
      String badpUserId = badp.getUserId();
      
      if (null == badpUserId || !badpUserId.equalsIgnoreCase(userId) || 
          null == loginToken || loginToken.isEmpty() || 
          !badp.hasExpirationDate() || expiry.isBefore(now.getMillis()) ) {
        log.error("user error: login-token login is invalid. basicAuthorizedDeviceProto="
            + badp);
        responseBuilder.setStatus(LoginResponseStatus.INVALID_LOGIN_TOKEN);
        return false;
      } else {
        return true;
      }
    }
    
    String facebookId = sender.getFacebookId();
    if (LoginType.FACEBOOK == lt) {
      if (null == facebookId || facebookId.isEmpty()) {
        log.error("user error: facebookId is invalid. facebookId=" + facebookId);
        responseBuilder.setStatus(LoginResponseStatus.INVALID_FACEBOOK_ID);
        return false;
      } else {
        return true;
      }
    }
    
    String email = sender.getEmail();
    String password = sender.getPassword();
    if (LoginType.EMAIL_PASSWORD == lt) {
      if (null == email || null == password || email.isEmpty() || password.isEmpty() ||
          !EmailUtil.isValidEmailAddressFormat(email)) {
        log.error("user error: email, password invalid. email=" + email);
        responseBuilder.setStatus(LoginResponseStatus.INVALID_EMAIL_PASSWORD);
        return false;
      } else {
        return true;
      }
    }
    
    String nameStrangersSee = sender.getNameStrangersSee();
    if (LoginType.NO_CREDENTIALS == lt) {
      if (null == nameStrangersSee || nameStrangersSee.isEmpty()) {
        log.error("user error: nameStrangersSee not set. nameStrangersSee=" + nameStrangersSee);
        responseBuilder.setStatus(LoginResponseStatus.INVALID_NO_CREDENTIALS);
        return false;
      } else {
        return true;
      }
    }
    log.error("unexpected error: loginType=" + lt + ", sender=" + sender + ", now=" + now);
    return false;
  }
  
  private boolean isValidRequest(Builder responseBuilder, 
      BasicUserProto sender, LoginType lt, DateTime now) {
    if (LoginType.LOGIN_TOKEN == lt) {
      return isValidLoginToken(responseBuilder, sender, now);
    }
    if (LoginType.FACEBOOK == lt) {
      return isValidFacebookLogin(responseBuilder, sender);
    }
    if (LoginType.EMAIL_PASSWORD == lt) {
      return isValidEmailPasswordLogin(responseBuilder, sender);
    }
    if (LoginType.NO_CREDENTIALS == lt) {
      return isValidNoCredentialsLogin(responseBuilder, sender);
    }
    log.error("unexpected error: loginType=" + lt);
    return false;
  }
  
  private boolean isValidLoginToken(Builder responseBuilder, BasicUserProto sender, DateTime now) {
    log.info("login token validation");
    BasicAuthorizedDeviceProto badp = sender.getBadp();
    String userId = badp.getUserId();
    String udid = badp.getUdid();
    String loginToken = badp.getLoginToken();
    
    //soo many things that can be checked (should be checked?)...(e.g. all values client sent match db)
    //could search by the BasicAuthorizedDeviceId...
    AuthorizedDevice bad = getAuthorizedDeviceService().checkForExistingAuthorizedDevice(userId, udid);
    //checking the token client sent matches token in database, and db/client-sent user ids match
    if (null != bad && null != bad.getToken() && bad.getToken().equals(loginToken) &&
        null != userId && bad.getUserId().equals(userId)) {
      return true;
    }
    log.error("unexpected error: authorizedDevice in the db=" + bad + ", sender=" + sender);
    responseBuilder.setStatus(LoginResponseStatus.INVALID_LOGIN_TOKEN);
    return false;
  }
  
  private boolean isValidFacebookLogin(Builder responseBuilder, BasicUserProto sender) {
    log.info("facebook login validation");
    String facebookId = sender.getFacebookId();
    String nameNull = null;
    String emailNull = null;
    String udidNull = null;
    List<User> userList = userSignupService.checkForExistingUser(facebookId, nameNull,
        emailNull, udidNull);
    if (null != userList && userList.size() == 1) {
      //could check if some values matched...but what if user deleted app
      return true;
    }
    log.error("unexpected error: users in db with facebookId=" + facebookId + 
        ",  userList" + userList);
    responseBuilder.setStatus(LoginResponseStatus.INVALID_FACEBOOK_ID);
    return false;
  }
  
  private boolean isValidEmailPasswordLogin(Builder responseBuilder, BasicUserProto sender) {
    log.info("email, password validation");
    //verify said person exists and email, password match
    String email = sender.getEmail();
    String password = sender.getPassword();
    String nameStrangersSee = sender.getNameStrangersSee();
    
    String facebookIdNull = null;
    String udidNull = null;
    
    List<User> userList = userSignupService.checkForExistingUser(facebookIdNull, nameStrangersSee,
        email, udidNull);
    if (null == userList || userList.size() != 1) {
      //don't want to print out password
      log.error("(?)user error: senderEmail=" + email + ", nameStrangersSee=" + nameStrangersSee
          + ", (in db) userList=" + userList);
      responseBuilder.setStatus(LoginResponseStatus.FAIL_OTHER);
      return false;
    }
    User inDb = userList.get(0);
    //check the user's email password match
    if (loginService.validCredentials(inDb, nameStrangersSee, email, password)) {
      return true;
    }
    
    log.error("user error: incorrect email and password. email=" + email);
    responseBuilder.setStatus(LoginResponseStatus.INVALID_EMAIL_PASSWORD);
    return false;
  }
  
  private boolean isValidNoCredentialsLogin(Builder responseBuilder, BasicUserProto sender) {
    log.info("no credentials (aka just name) validation") ;
    String facebookIdNull = null;
    String nameStrangersSee = sender.getNameStrangersSee();
    String emailNull = null;
    String udidNull = null;
    
    List<User> userList = userSignupService.checkForExistingUser(facebookIdNull, nameStrangersSee,
        emailNull, udidNull);
    if (null == userList || userList.size() != 1) {
      responseBuilder.setStatus(LoginResponseStatus.FAIL_OTHER);
      log.error("(?)user error: nameStrangersSee=" + nameStrangersSee + ", (in db) userList="
      + userList);
      return false;
    }
    
    return true;
  }
  
  private boolean writeChangesToDb(Builder responseBuilder, BasicUserProto sender, LoginType lt,
      List<String> facebookFriendIds, boolean initializeAccount, DateTime now) {
    if (initializeAccount) {
      //give the user all the power ups, coins and stuff
    }
    //TODO: RECORD THE USER LOGGING IN
    // GET FACEBOOK FRIENDS IF LOGIN TYPE IS FACEBOOK AND 
    // CONSTRUCT THE USER (EQUIPS, CURRENCY AND ALL)
    // CONSTRUCT THE NEW TRIVIA QUESTIONS
    // GET ALL THE COMPLETED GAMES
    // GET ALL THE GAMES THAT ARE THE USER'S TURN
    // GET ALL THE GAMES THAT ARE THE OPPONENT'S TURN
    
    if (LoginType.FACEBOOK == lt) {
      
    }
    
    return false;
  }
  
  public UserSignupService getService() {
    return userSignupService;
  }
  
  public void setService(UserSignupService service) {
    this.userSignupService = service;
  }
  
  public AuthorizedDeviceService getAuthorizedDeviceService() {
    return authorizedDeviceService;
  }

  public void setAuthorizedDeviceService(
      AuthorizedDeviceService authorizedDeviceService) {
    this.authorizedDeviceService = authorizedDeviceService;
  }
 
  public LoginService getLoginService() {
    return loginService;
  }
  
  public void setLoginService(LoginService loginService) {
    this.loginService = loginService;
  }

  
}