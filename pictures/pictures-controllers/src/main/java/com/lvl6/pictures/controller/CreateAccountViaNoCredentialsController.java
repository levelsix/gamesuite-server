package com.lvl6.pictures.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Component;

import com.lvl6.gamesuite.common.controller.EventController;
import com.lvl6.pictures.eventprotos.CreateAccountEventProto.CreateAccountResponseProto;
import com.lvl6.pictures.eventprotos.CreateAccountEventProto.CreateAccountResponseProto.Builder;
import com.lvl6.pictures.eventprotos.CreateAccountEventProto.CreateAccountResponseProto.CreateAccountStatus;
import com.lvl6.pictures.eventprotos.CreateAccountEventProto.CreateAccountViaNoCredentialsRequestProto;
import com.lvl6.gamesuite.common.events.RequestEvent;
import com.lvl6.gamesuite.common.noneventprotos.CommonEventProtocolProto.CommonEventProtocolRequest;
import com.lvl6.pictures.noneventprotos.UserProto.BasicUserProto;
import com.lvl6.pictures.controller.utils.CreateNoneventProtoUtils;
import com.lvl6.pictures.events.request.CreateAccountViaNoCredentialsRequestEvent;
import com.lvl6.pictures.events.response.CreateAccountViaEmailResponseEvent;
import com.lvl6.gamesuite.common.po.AuthorizedDevice;
import com.lvl6.gamesuite.common.po.User;
import com.lvl6.gamesuite.common.services.authorizeddevice.AuthorizedDeviceService;
import com.lvl6.gamesuite.common.services.user.UserSignupService;

@Component @DependsOn("gameServer") public class CreateAccountViaNoCredentialsController extends EventController {
  
  private static Logger log = LoggerFactory.getLogger(new Object() { }.getClass().getEnclosingClass());

  @Autowired
  protected UserSignupService userSignupService;
  
  @Autowired
  protected AuthorizedDeviceService authorizedDeviceService;
  
  @Autowired
  protected CreateNoneventProtoUtils noneventProtoUtils;


  @Override
  public RequestEvent createRequestEvent() {
    return new CreateAccountViaNoCredentialsRequestEvent();
  }

  @Override
  public int getEventType() {
    return CommonEventProtocolRequest.C_CREATE_ACCOUNT_VIA_NO_CREDENTIALS_EVENT_VALUE;
  }

  @Override
  protected void processRequestEvent(RequestEvent event) throws Exception {
    CreateAccountViaNoCredentialsRequestProto reqProto = 
        ((CreateAccountViaNoCredentialsRequestEvent) event).getCreateAccountViaNoCredentialsRequestProto();
    String nameStrangersSee = reqProto.getNameStrangersSee();
    String udid = reqProto.getUdid();
    String deviceId = reqProto.getDeviceId();

    //response to send back to client
    CreateAccountResponseProto.Builder responseBuilder = CreateAccountResponseProto.newBuilder();
    responseBuilder.setStatus(CreateAccountStatus.FAIL_OTHER);
    
    boolean validRequestArgs = isValidRequestArguments(responseBuilder, reqProto, nameStrangersSee, udid);
    boolean validRequest = false;
    boolean success = false;
    
    
    if (validRequestArgs) {
      validRequest = isValidRequest(responseBuilder, nameStrangersSee, udid, deviceId);
    }
    
    if(validRequest) {
      success = writeChangesToDb(responseBuilder, nameStrangersSee, udid, deviceId);
    }
    
    if(success) {
      responseBuilder.setStatus(CreateAccountStatus.SUCCESS_ACCOUNT_CREATED);
    }
    
    CreateAccountResponseProto resProto = responseBuilder.build();
    //autowire or use new()...
    CreateAccountViaEmailResponseEvent resEvent =  new CreateAccountViaEmailResponseEvent(udid);
    resEvent.setTag(event.getTag());
    resEvent.setCreateAccountResponseProto(resProto);
    
    log.info("Writing event: " + resEvent);
    getEventWriter().processPreDBResponseEvent(resEvent, udid);
  }

  private boolean isValidRequestArguments(Builder responseBuilder, CreateAccountViaNoCredentialsRequestProto request,
      String nameStrangersSee, String udid) {
    if (!(request.hasNameStrangersSee()) || nameStrangersSee.isEmpty()) {
      responseBuilder.setStatus(CreateAccountStatus.FAIL_INVALID_NAME);
      log.error("unexpected error: no nameStrangersSee provided. nameStrangersSee:" + nameStrangersSee +
          ", udid:" + udid);
      return false;
    }
    if (!authorizedDeviceService.isValidUdid(udid)) {
      responseBuilder.setStatus(CreateAccountStatus.FAIL_INVALID_UDID);
      log.error("unexpected error: invalid udid provided. udid=" + udid);
      return false;
    }
    
    return true;
  }
  
  private boolean isValidRequest(Builder responseBuilder, String nameStrangersSee,
      String udid, String deviceId) {
    
    String facebookIdNull = null;
    String emailNull = null;
    String udidNull = null;
    List<User> existing = userSignupService.checkForExistingUser(facebookIdNull, nameStrangersSee, emailNull, udidNull);
    
    if (null != existing && !existing.isEmpty()) {
      for (User u: existing) {
        if (nameStrangersSee.equalsIgnoreCase(u.getNameStrangersSee())) { //ignore case for now
          responseBuilder.setStatus(CreateAccountStatus.FAIL_DUPLICATE_NAME);
          log.error("user error: Either name in use nameStrangersSee or user already has " +
              "account with us. user=" + existing);
          return false;
        } else {
          //maybe just ignore instead and not treat this as a fail...
          log.error("unexpected error: user returned does not have same nameStrangersSee, nor email. user=" + u +
              " args=[nameStrangersSee=" + nameStrangersSee + ", udid=" + udid + ", deviceId=" + deviceId);
          responseBuilder.setStatus(CreateAccountStatus.FAIL_OTHER);
          return false;
        }
      }
    }
    
    return true;
  }
  
  private boolean writeChangesToDb(Builder responseBuilder, String nameStrangersSee,
      String udid, String deviceId) {
    boolean success = false;
    
    String nameFriendsSeeNull = null;
    String emailNull = null;
    String passwordNull = null;
    String facebookId = null;
    String userId = null;
    User newUser = null;
    AuthorizedDevice  ad = null;
    try {
      //create the new user
      newUser = userSignupService.signup(nameStrangersSee, nameFriendsSeeNull, emailNull, passwordNull, facebookId);
      //maybe some error checking here...
      
      //need to record the device for the user
      userId = newUser.getId();
      ad = authorizedDeviceService.registerNewAuthorizedDevice(userId, udid, deviceId);
     
      BasicUserProto bp = noneventProtoUtils.createBasicUserProto(newUser, ad);
      responseBuilder.setRecipient(bp);
      
      success = true;
    } catch (Exception e) {
      log.error("failed to create user or device. user=" + newUser + ", authorizedDevice="+ ad, e);
    }
    return success;
  }
  
  public UserSignupService getService() {
    return userSignupService;
  }
  
  public void setService(UserSignupService service) {
    this.userSignupService = service;
  }
  
  public CreateNoneventProtoUtils getNoneventProtoUtils() {
    return noneventProtoUtils;
  }

  public void setNoneventProtoUtils(CreateNoneventProtoUtils noneventProtoUtils) {
    this.noneventProtoUtils = noneventProtoUtils;
  }

}