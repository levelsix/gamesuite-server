package com.lvl6.pictures.controller;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.lvl6.gamesuite.common.controller.EventController;
import com.lvl6.gamesuite.common.events.RequestEvent;
import com.lvl6.gamesuite.common.po.User;
import com.lvl6.gamesuite.common.services.user.UserSignupService;
import com.lvl6.pictures.controller.utils.CreateNoneventProtoUtils;
import com.lvl6.pictures.eventprotos.SearchForUserEventProto.SearchForUserRequestProto;
import com.lvl6.pictures.eventprotos.SearchForUserEventProto.SearchForUserResponseProto;
import com.lvl6.pictures.eventprotos.SearchForUserEventProto.SearchForUserResponseProto.Builder;
import com.lvl6.pictures.eventprotos.SearchForUserEventProto.SearchForUserResponseProto.SearchForUserStatus;
import com.lvl6.pictures.events.request.SearchForUserRequestEvent;
import com.lvl6.pictures.events.response.SearchForUserResponseEvent;
import com.lvl6.pictures.noneventprotos.PicturesEventProtocolProto.PicturesEventProtocolRequest;
import com.lvl6.pictures.noneventprotos.UserProto.BasicUserProto;
import com.lvl6.pictures.services.currency.CurrencyService;

@Component
public class SearchForUserController extends EventController {
  
  private static Logger log = LoggerFactory.getLogger(new Object() { }.getClass().getEnclosingClass());
  
  @Autowired
  protected CreateNoneventProtoUtils createNoneventProtoUtils;
  
  @Autowired
  protected CurrencyService currencyService; 
  
  @Autowired
  protected UserSignupService userSignupService;
  
  
  
  @Override
  public RequestEvent createRequestEvent() {
    return new SearchForUserRequestEvent();
  }

  @Override
  public int getEventType() {
    return PicturesEventProtocolRequest.C_SEARCH_FOR_USER_EVENT_VALUE;
  }
  
  @Override
  protected void processRequestEvent(RequestEvent event) throws Exception {
    //stuff client sent
    SearchForUserRequestProto reqProto = 
        ((SearchForUserRequestEvent) event).getSearchForUserRequestProto();
    BasicUserProto sender = reqProto.getSender();
    String userId = sender.getUserId();
    String nameOfPerson = reqProto.getNameOfPerson();
    
    //response to send back to client
    Builder responseBuilder = SearchForUserResponseProto.newBuilder();
    responseBuilder.setRecipient(sender);
    responseBuilder.setStatus(SearchForUserStatus.FAIL_OTHER);
    
    SearchForUserResponseEvent resEvent = new SearchForUserResponseEvent(userId);
    resEvent.setTag(event.getTag());
    
    try {
      List<User> inDbList = new ArrayList<User>(); //will hold the desired user
      boolean validRequest = isValidRequest(responseBuilder, sender,
          nameOfPerson, inDbList);
      
      if (validRequest) {
        User inDb = inDbList.get(0);
        responseBuilder.setNameOfPersonId(inDb.getId());
        responseBuilder.setStatus(SearchForUserStatus.SUCCESS);
      }
      
      //write to client
      resEvent.setSearchForUserResponseProto(responseBuilder.build());
      log.info("Writing event: " + resEvent);
      getEventWriter().handleEvent(resEvent);
      
    } catch (Exception e) {
      log.error("exception in SearchForUserController processRequestEvent", e);
      
      try {
        //try to tell client that something failed
        responseBuilder.setStatus(SearchForUserStatus.FAIL_OTHER);
        resEvent.setSearchForUserResponseProto(responseBuilder.build());
        getEventWriter().handleEvent(resEvent);
        
      } catch (Exception e2) {
        log.error("exception in SearchForUserController processRequestEvent", e2);
      }
    }
  }

  private boolean isValidRequest(Builder responseBuilder, BasicUserProto sender,
      String nameOfPerson, List<User> results) {
    if (null == nameOfPerson || nameOfPerson.isEmpty()) {
      log.error("user error: invalid name. nameOfPerson=" + nameOfPerson);
      return false;
    }
    String facebookIdNull = null;
    String emailNull = null;
    String udidNull = null;
    List<User> inDbList = getUserSignupService().checkForExistingUser(facebookIdNull,
        nameOfPerson, emailNull, udidNull);
    
    if (null == inDbList || inDbList.isEmpty()) {
      log.info("user error: person not found. nameOfPerson=" + nameOfPerson);
      responseBuilder.setStatus(SearchForUserStatus.FAIL_PERSON_NOT_FOUND);
      return false;
    }
    
    if (inDbList.size() != 1) {
      log.error("unexpected error: duplicate users. inDbList=" + inDbList);
      return false;
    }
    
    results.addAll(inDbList);
    return true;
  }
  
  public CreateNoneventProtoUtils getCreateNoneventProtoUtils() {
    return createNoneventProtoUtils;
  }

  public void setCreateNoneventProtoUtils(
      CreateNoneventProtoUtils createNoneventProtoUtils) {
    this.createNoneventProtoUtils = createNoneventProtoUtils;
  }

  public CurrencyService getCurrencyService() {
    return currencyService;
  }

  public void setCurrencyService(CurrencyService currencyService) {
    this.currencyService = currencyService;
  }

  public UserSignupService getUserSignupService() {
    return userSignupService;
  }

  public void setUserSignupService(UserSignupService userSignupService) {
    this.userSignupService = userSignupService;
  }

}