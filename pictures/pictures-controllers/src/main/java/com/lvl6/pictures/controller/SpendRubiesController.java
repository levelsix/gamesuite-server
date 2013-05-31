package com.lvl6.pictures.controller;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.lvl6.gamesuite.common.controller.EventController;
import com.lvl6.gamesuite.common.events.RequestEvent;
import com.lvl6.gamesuite.common.po.User;
import com.lvl6.gamesuite.common.services.user.LoginService;
import com.lvl6.pictures.controller.utils.CreateNoneventProtoUtils;
import com.lvl6.pictures.eventprotos.SpendRubiesEventProto.SpendRubiesRequestProto;
import com.lvl6.pictures.eventprotos.SpendRubiesEventProto.SpendRubiesResponseProto;
import com.lvl6.pictures.eventprotos.SpendRubiesEventProto.SpendRubiesResponseProto.Builder;
import com.lvl6.pictures.eventprotos.SpendRubiesEventProto.SpendRubiesResponseProto.SpendRubiesStatus;
import com.lvl6.pictures.events.request.SpendRubiesRequestEvent;
import com.lvl6.pictures.events.response.SpendRubiesResponseEvent;
import com.lvl6.pictures.noneventprotos.PicturesEventProtocolProto.PicturesEventProtocolRequest;
import com.lvl6.pictures.noneventprotos.UserProto.BasicUserProto;
import com.lvl6.pictures.noneventprotos.UserProto.UserCurrencyProto;
import com.lvl6.pictures.po.Currency;
import com.lvl6.pictures.services.currency.CurrencyService;

public class SpendRubiesController extends EventController {
  
  private static Logger log = LoggerFactory.getLogger(new Object() { }.getClass().getEnclosingClass());
  
  @Autowired
  protected CreateNoneventProtoUtils createNoneventProtoUtils;
  
  @Autowired
  protected CurrencyService currencyService; 
  
  @Autowired
  protected LoginService loginService;
  
  
  
  @Override
  public RequestEvent createRequestEvent() {
    return new SpendRubiesRequestEvent();
  }

  @Override
  public int getEventType() {
    return PicturesEventProtocolRequest.C_SPEND_RUBIES_EVENT_VALUE;
  }
  
  @Override
  protected void processRequestEvent(RequestEvent event) throws Exception {
    //stuff client sent
    SpendRubiesRequestProto reqProto = 
        ((SpendRubiesRequestEvent) event).getSpendRubiesRequestProto();
    BasicUserProto sender = reqProto.getSender();
    String userId = sender.getUserId();
    int amountSpent = reqProto.getAmountSpent();
    
    //response to send back to client
    Builder responseBuilder = SpendRubiesResponseProto.newBuilder();
    responseBuilder.setRecipient(sender);
    responseBuilder.setStatus(SpendRubiesStatus.FAIL_OTHER);
    
    SpendRubiesResponseEvent resEvent = new SpendRubiesResponseEvent(userId);
    resEvent.setTag(event.getTag());
    
    try {
      //get the user and his currency
      User inDb = getLoginService().getUserById(userId);
      List<Currency> monies = new ArrayList<Currency>();
      
      boolean validRequest = isValidRequest(responseBuilder, sender, inDb,
          amountSpent, monies);
      
      boolean success = false;
      if (validRequest) {
        //record that the user's rubies changed
        success = writeChangesToDb(userId, monies, amountSpent);
      }
      
      if (success) {
        responseBuilder.setStatus(SpendRubiesStatus.SUCCESS);
        Currency c = monies.get(0);
        UserCurrencyProto ucp = getCreateNoneventProtoUtils().createUserCurrencyProto(c);
        responseBuilder.setCurrentFunds(ucp);
      }
      
      //write to client
      resEvent.setSpendRubiesResponseProto(responseBuilder.build());
      log.info("Writing event: " + resEvent);
      getEventWriter().handleEvent(resEvent);
      
    } catch (Exception e) {
      log.error("exception in SpendRubiesController processRequestEvent", e);
      
      try {
        //try to tell client that something failed
        responseBuilder.setStatus(SpendRubiesStatus.FAIL_OTHER);
        resEvent.setSpendRubiesResponseProto(responseBuilder.build());
        getEventWriter().handleEvent(resEvent);
        
      } catch (Exception e2) {
        log.error("exception in SpendRubiesController processRequestEvent", e2);
      }
    }
  }

  private boolean isValidRequest(Builder responseBuilder, BasicUserProto sender,
      User inDb, int amountSpent, List<Currency> monies) {
    if (null == inDb) {
      log.error("unexpected error: no user exists. sender=" + sender);
      return false;
    }
    //make sure client didn't change his time
//    if (!getTimeUtils().isSynchronizedWithServerTime(clientDate)) {
//      log.error("user error: client time diverges from server time. clientTime="
//          + clientDate + ", approximateServerTime=" + new Date());
//      responseBuilder.setStatus(SpendRubiesStatus.FAIL_CLIENT_TOO_APART_FROM_SERVER_TIME);
//      return false;
//    }

    //check if user has enough rubies
    String userId = inDb.getId();
    Currency c = getCurrencyService().getCurrencyForUser(userId); 
    int currentRubies = c.getRubies();
    if (amountSpent > currentRubies) {
      log.error("user error: client trying to spend more than what he has." +
      		" currency=" + c + ";\t amountSpent=" + amountSpent);
      responseBuilder.setStatus(SpendRubiesStatus.FAIL_NOT_ENOUGH_RUBIES);
      return false;
    }
    
    monies.add(c);
    return true;
  }
  
  private boolean writeChangesToDb(String userId, List<Currency> monies,
      int amountSpent) {
    try {
      Currency money = monies.get(0);
      getCurrencyService().updateRubiesForUser(money, amountSpent);
      return true;
      
    } catch (Exception e) {
      log.error("unexpected error: problem with saving to db.", e);
    }
    return false;
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

  public LoginService getLoginService() {
    return loginService;
  }

  public void setLoginService(LoginService loginService) {
    this.loginService = loginService;
  }

//  public TimeUtils getTimeUtils() {
//    return timeUtils;
//  }
//
//  public void setTimeUtils(TimeUtils timeUtils) {
//    this.timeUtils = timeUtils;
//  }

}