package com.lvl6.pictures.services.currency;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;

import com.lvl6.gamesuite.common.controller.utils.TimeUtils;
import com.lvl6.pictures.dao.CurrencyDao;
import com.lvl6.pictures.po.Currency;
import com.lvl6.pictures.properties.PicturesPoConstants;

public class CurrencyServiceImpl implements CurrencyService {
  
  @Autowired
  protected CurrencyDao currencyDao;
  
  @Autowired
  protected TimeUtils timeUtils;

  @Override
  public Currency initializeUserCurrency(String userId, Date now) {
    Currency monies = new Currency();
    monies.setRubies(PicturesPoConstants.CURRENCY__DEFAULT_INITIAL_RUBIES);
    monies.setTokens(PicturesPoConstants.CURRENCY__DEFAULT_INITIAL_TOKENS);
    monies.setLastTokenRefillTime(now);
    monies.setUserId(userId);
    
    currencyDao.save(monies);
    return monies;
  }
  
  @Override
  public Currency getCurrencyForUser(String userId) {
    return currencyDao.findByUserId(userId);
  }
  
  @Override
  public boolean canRegenerateToken(Date lastRefillTime, Date now) {
    int minDifference = timeUtils.numMinutesDifference(lastRefillTime, now);
    
    int regenTime = PicturesPoConstants.CURRENCY__MINUTES_FOR_TOKEN_REGENERATION;
    if (minDifference > regenTime) {
      return true;
    } else {
      return false;
    }
  }

  @Override
  public int numTokensRegenerated(Currency c, Date now) {
    Date lastRefillTime = c.getLastTokenRefillTime();
    int diff = timeUtils.numMinutesDifference(lastRefillTime, now);
    int numTokensRefilled = 
        diff / PicturesPoConstants.CURRENCY__MINUTES_FOR_TOKEN_REGENERATION;
    
    //user can't get more than the max amount of tokens
    int maxTokens = PicturesPoConstants.CURRENCY__DEFAULT_MAX_TOKENS;
    int newAmount = c.getTokens() + numTokensRefilled;
    int receivedTokens = Math.min(maxTokens, newAmount);
    
    return receivedTokens;
  }
  
  @Override
  public void refillTokensForUser(Currency c, int newTokenAmount,
      Date timeRefilled) {
    c.setLastTokenRefillTime(timeRefilled);
    c.setTokens(newTokenAmount);
    
    getCurrencyDao().save(c);
  }
  
  @Override
  public CurrencyDao getCurrencyDao() {
    return currencyDao;
  }
  @Override
  public void setCurrencyDao(CurrencyDao currencyDao) {
    this.currencyDao = currencyDao;
  }
  
}