package com.lvl6.pictures.services.currency;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;

import com.lvl6.pictures.dao.CurrencyDao;
import com.lvl6.pictures.po.Currency;
import com.lvl6.pictures.properties.PicturesPoConstants;

public class CurrencyServiceImpl implements CurrencyService {
  
  @Autowired
  protected CurrencyDao currencyDao;

  public Currency initializeUserCurrency(String userId, Date now) {
    Currency monies = new Currency();
    monies.setRubies(PicturesPoConstants.USER__DEFAULT_INITIAL_RUBIES);
    monies.setTokens(PicturesPoConstants.USER__DEFAULT_INITIAL_TOKENS);
    monies.setLastTokenRefillTime(now);
    monies.setUserId(userId);
    
    currencyDao.save(monies);
    return monies;
  }
  
  public Currency getCurrencyForUser(String userId) {
    return currencyDao.findByUserId(userId);
  }
  
  public CurrencyDao getCurrencyDao() {
    return currencyDao;
  }

  public void setCurrencyDao(CurrencyDao currencyDao) {
    this.currencyDao = currencyDao;
  }
  
}