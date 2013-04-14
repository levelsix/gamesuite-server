package com.lvl6.gamesuite.common.controller;

//import javax.mail.internet.InternetAddress;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Component;

import com.lvl6.gamesuite.common.eventprotos.CreateAccountEventProto.CreateAccountRequestProto;
import com.lvl6.gamesuite.common.eventprotos.CreateAccountEventProto.CreateAccountResponseProto;
import com.lvl6.gamesuite.common.eventprotos.CreateAccountEventProto.CreateAccountResponseProto.Builder;
import com.lvl6.gamesuite.common.eventprotos.CreateAccountEventProto.CreateAccountResponseProto.CreateAccountStatus;
import com.lvl6.gamesuite.common.events.RequestEvent;
import com.lvl6.gamesuite.common.events.request.CreateAccountRequestEvent;
import com.lvl6.gamesuite.common.noneventprotos.CommonEventProtocolsProto.CommonEventProtocolRequest;
import com.lvl6.gamesuite.common.services.user.UserSignupService;

@Component @DependsOn("gameServer") public class CreateAccountController extends EventController {
  
  private static Logger log = LoggerFactory.getLogger(new Object() { }.getClass().getEnclosingClass());

  @Autowired
  protected UserSignupService service;
  
//  @Autowired
//  protected InternetAddress iAddress;

  @Override
  public RequestEvent createRequestEvent() {
    return new CreateAccountRequestEvent();
  }

  @Override
  public int getEventType() {
    return CommonEventProtocolRequest.C_CREATE_ACCOUNT_EVENT_VALUE;
  }

  @Override
  protected void processRequestEvent(RequestEvent event) throws Exception {
    CreateAccountRequestProto reqProto = ((CreateAccountRequestEvent) event).getCreateAccountRequestProto();
   
    //response to send back to client
    CreateAccountResponseProto.Builder responseBuilder = CreateAccountResponseProto.newBuilder();
    boolean isValid = isValidRequest(responseBuilder, reqProto);
    
    if (isValid) {
      
    }
  }

  public boolean isValidRequest(Builder responseBuilder, CreateAccountRequestProto request) {
    String facebookId = request.getFacebookId();
    String name = request.getName();
    String email = request.getEmail();
    String password = request.getPassword();
    String udid = request.getUdid();
    
    if (!(request.hasName()) || name.isEmpty()) {
      responseBuilder.setStatus(CreateAccountStatus.FAIL_INVALID_NAME);
      log.error("unexpected error: no name provided. facebookId:" + facebookId
      		+ ", name:" + name + ", email:" + email + ", udid:" + udid);
      return false;
    }
    if (!(request.hasEmail()) || email.isEmpty()) {
      responseBuilder.setStatus(CreateAccountStatus.FAIL_INVALID_EMAIL);
      log.error("unexpected error: no name provided. facebookId:" + facebookId
          + ", name:" + name + ", email:" + email + ", udid:" + udid);
      return false;
    }
    if ( ( !(request.hasFacebookId()) || facebookId.isEmpty() ) 
        && (!(request.hasPassword()) || password.isEmpty()) ) {
      //facebook id and password not provided 
      responseBuilder.setStatus(CreateAccountStatus.FAIL_OTHER);
      return false;
    }
    
    return true;
  }
  
  public UserSignupService getService() {
    return service;
  }
  
  public void setService(UserSignupService service) {
    this.service = service;
  }
  
}