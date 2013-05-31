package com.lvl6.pictures.services.currency;

import java.util.Date;
import java.util.Map;
import java.util.Set;

import com.lvl6.pictures.dao.CurrencyDao;
import com.lvl6.pictures.po.Currency;

public interface CurrencyService {
  
  public abstract Currency initializeUserCurrency(String userId, Date now);
  
  public abstract Currency getCurrencyForUser(String userId);
  
  public abstract Map<String, Currency> getCurrenciesByUserIds(Set<String> ids);
  
  public abstract boolean canRegenerateToken(Date lastRefillTime, Date now);
  
  public abstract int numTokensRegenerated(Currency c, Date now);
  
  public abstract void updateTokensForUser(Currency c, int newTokenAmount,
      Date timeRefilled);
  
  public abstract void spendTokenForUser(Currency c, Date startDate);
  
  public abstract void updateRubiesForUser(Currency c, int amountSpent);
  
  public abstract CurrencyDao getCurrencyDao();
  
  public abstract void setCurrencyDao(CurrencyDao currencyDao);
  
}