package com.lvl6.pictures.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.lvl6.gamesuite.common.controller.EventController;
import com.lvl6.gamesuite.common.controller.utils.TimeUtils;
import com.lvl6.gamesuite.common.events.RequestEvent;
import com.lvl6.gamesuite.common.po.User;
import com.lvl6.gamesuite.common.services.user.LoginService;
import com.lvl6.pictures.eventprotos.RefillTokensByWaitingEventProto.RefillTokensByWaitingRequestProto;
import com.lvl6.pictures.eventprotos.RefillTokensByWaitingEventProto.RefillTokensByWaitingResponseProto;
import com.lvl6.pictures.eventprotos.RefillTokensByWaitingEventProto.RefillTokensByWaitingResponseProto.Builder;
import com.lvl6.pictures.eventprotos.RefillTokensByWaitingEventProto.RefillTokensByWaitingResponseProto.RefillTokensByWaitingStatus;
import com.lvl6.pictures.events.request.RefillTokensByWaitingRequestEvent;
import com.lvl6.pictures.events.response.RefillTokensByWaitingResponseEvent;
import com.lvl6.pictures.noneventprotos.PicturesEventProtocolProto.PicturesEventProtocolRequest;
import com.lvl6.pictures.noneventprotos.UserProto.BasicUserProto;
import com.lvl6.pictures.po.Currency;
import com.lvl6.pictures.properties.PicturesPoConstants;
import com.lvl6.pictures.services.currency.CurrencyService;

public class RefillTokensByWaitingController extends EventController {
  
  private static Logger log = LoggerFactory.getLogger(new Object() { }.getClass().getEnclosingClass());
  
  @Autowired
  protected LoginService loginService;
  
  @Autowired
  protected TimeUtils timeUtils;
  
  @Autowired
  protected CurrencyService currencyService; 
  
  @Override
  public RequestEvent createRequestEvent() {
    return new RefillTokensByWaitingRequestEvent();
  }

  @Override
  public int getEventType() {
    return PicturesEventProtocolRequest.C_REFILL_TOKENS_BY_WAITING_EVENT_VALUE;
  }
  
  @Override
  protected void processRequestEvent(RequestEvent event) throws Exception {
    //stuff client sent
    RefillTokensByWaitingRequestProto reqProto = 
        ((RefillTokensByWaitingRequestEvent) event).getRefillTokensByWaitingRequestProto();
    BasicUserProto sender = reqProto.getSender();
    String userId = sender.getUserId();
    long clientTime = reqProto.getCurTime();
    Date clientDate = new Date(clientTime);
    
    //response to send back to client
    Builder responseBuilder = RefillTokensByWaitingResponseProto.newBuilder();
    responseBuilder.setStatus(RefillTokensByWaitingStatus.FAIL_OTHER);
    RefillTokensByWaitingResponseEvent resEvent =
        new RefillTokensByWaitingResponseEvent(userId);
    resEvent.setTag(event.getTag());
    
    User inDb = getLoginService().getUserById(userId);
    List<Currency> monies = new ArrayList<Currency>();
    //validate request
    boolean validRequest = isValidRequest(responseBuilder, sender, inDb,
        clientDate, monies);
    
    boolean successful = false;
    if (validRequest) {
      successful = writeChangesToDb(monies, clientDate);
    }
    
    if (successful) {
      responseBuilder.setStatus(RefillTokensByWaitingStatus.SUCCESS);
    }
    
    //write to client
    resEvent.setRefillTokensByWaitingResponseProto(responseBuilder.build());
    
    log.info("Writing event: " + resEvent);
    getEventWriter().handleEvent(resEvent);
  }

  private boolean isValidRequest(Builder responseBuilder, BasicUserProto sender,
      User inDb, Date clientDate, List<Currency> monies) {
    if (null == inDb) {
      log.error("unexpected error: no user exists. sender=" + sender);
      return false;
    }
    if (!getTimeUtils().isSynchronizedWithServerTime(clientDate)) {
      log.error("user error: client time diverges from server time. clientTime="
          + clientDate + ", approximateServerTime=" + new Date());
      responseBuilder.setStatus(RefillTokensByWaitingStatus.FAIL_CLIENT_TOO_APART_FROM_SERVER_TIME);
      return false;
    }

    //check if user is already at max tokens
    String userId = inDb.getId();
    Currency c = getCurrencyService().getCurrencyForUser(userId); 
    int maxTokens = PicturesPoConstants.CURRENCY__DEFAULT_MAX_TOKENS;
    int currentTokens = c.getTokens();
    if (currentTokens == maxTokens) {
      log.error("unexpected error: client already has max tokens. currency=" +
          c + ";   maxTokens=" + maxTokens);
      responseBuilder.setStatus(RefillTokensByWaitingStatus.FAIL_ALREADY_MAX);
      return false;
    }
    
    //check if user can regenerate a token
    //current time > last refill time + time_to_regenerate
    Date lastRefillTime = c.getLastTokenRefillTime();
    if (!getCurrencyService().canRegenerateToken(lastRefillTime, clientDate)) {
      log.error("user error: tokens not ready for refill yet. clientTime=" +
          clientDate + ";      lastRefillTime=" + lastRefillTime +
          ";      numMinutesForTokenRefill=" +
          PicturesPoConstants.CURRENCY__MINUTES_FOR_TOKEN_REGENERATION);
      responseBuilder.setStatus(RefillTokensByWaitingStatus.FAIL_NOT_READY_YET);
      return false;
    }
    
    monies.add(c);
    return true;
  }
  
  private boolean writeChangesToDb(List<Currency> monies, Date clientDate) {
    try {
      Currency money = monies.get(0);
      int newTokenAmount =
          getCurrencyService().numTokensRegenerated(money, clientDate);
      getCurrencyService().refillTokensForUser(money, newTokenAmount, clientDate);
      return true;
      
    } catch (Exception e) {
      log.error("unexpected error: problem with saving to db.");
    }
    return false;
  }
  
  public LoginService getLoginService() {
    return loginService;
  }

  public void setLoginService(LoginService loginService) {
    this.loginService = loginService;
  }

  public TimeUtils getTimeUtils() {
    return timeUtils;
  }

  public void setTimeUtils(TimeUtils timeUtils) {
    this.timeUtils = timeUtils;
  }

  public CurrencyService getCurrencyService() {
    return currencyService;
  }

  public void setCurrencyService(CurrencyService currencyService) {
    this.currencyService = currencyService;
  }
  
}