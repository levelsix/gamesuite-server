package com.lvl6.pictures.services.currency;

import java.util.Date;

import com.lvl6.pictures.dao.CurrencyDao;
import com.lvl6.pictures.po.Currency;

public interface CurrencyService {
  
  public abstract Currency initializeUserCurrency(String userId, Date now);
  
  public abstract Currency getCurrencyForUser(String userId);
  
  public abstract boolean canRegenerateToken(Date lastRefillTime, Date now);
  
  public abstract int numTokensRegenerated(Currency c, Date now);
  
  public abstract void refillTokensForUser(Currency c, int newTokenAmount,
      Date timeRefilled);
  
  public abstract CurrencyDao getCurrencyDao();
  
  public abstract void setCurrencyDao(CurrencyDao currencyDao);
  
}