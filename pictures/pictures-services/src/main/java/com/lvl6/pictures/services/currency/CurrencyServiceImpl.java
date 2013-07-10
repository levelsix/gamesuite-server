package com.lvl6.pictures.services.currency;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.lvl6.gamesuite.common.controller.utils.TimeUtils;
import com.lvl6.pictures.dao.CurrencyDao;
import com.lvl6.pictures.po.Currency;
import com.lvl6.pictures.properties.PicturesPoConstants;

@Component
public class CurrencyServiceImpl implements CurrencyService {
    
    private static Logger log = LoggerFactory.getLogger(new Object() { }.getClass().getEnclosingClass());

    @Resource
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

	monies = currencyDao.save(monies);
	return monies;
    }

    @Override
    public Currency getCurrencyForUser(String userId) {
	return currencyDao.findByUserId(userId);
    }

    @Override
    public Map<String, Currency> getCurrenciesByUserIds(Set<String> ids) {
	Map<String, Currency> currenciesByUserIds = new HashMap<String, Currency>();
	List<Currency> monies = currencyDao.findByUserIdIn(ids);

	for (Currency money : monies) {
	    String userId = money.getUserId();
	    currenciesByUserIds.put(userId, money);
	}
	return currenciesByUserIds;
    }

    @Override
    public boolean canRegenerateToken(Date lastRefillTime, Date now) {
	//int minDifference = timeUtils.numMinutesDifference(lastRefillTime, now);
	int secDifference = timeUtils.numSecondsDifference(lastRefillTime, now);
	//log.info("\t minDifference=" + minDifference);
	log.info("\t secDifference=" + secDifference);
	
	//int regenMin = PicturesPoConstants.CURRENCY__MINUTES_FOR_TOKEN_REGENERATION;
	int regenSec = PicturesPoConstants.CURRENCY__SECONDS_FOR_TOKEN_REGENERATION;
	if (secDifference >= regenSec) {
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
    public void updateTokensForUser(Currency c, int newTokenAmount,
	    Date timeRefilled) {
	if (null != timeRefilled) {
	    c.setLastTokenRefillTime(timeRefilled);
	}
	c.setTokens(newTokenAmount);

	getCurrencyDao().save(c);
    }

    public void spendTokenForUser(Currency c, Date startDate) {
	//if the user's tokens is max set user's lastTokenRefilledTime
	//to startDate
	int maxTokens = PicturesPoConstants.CURRENCY__DEFAULT_MAX_TOKENS;
	int currentTokens = c.getTokens();
	Date timeRefilled = null;
	if (currentTokens == maxTokens) {
	    timeRefilled = startDate;
	}
	int newTokenAmount = currentTokens - 1;

	updateTokensForUser(c, newTokenAmount, timeRefilled);
    }

    public void relativelyUpdateRubiesForUser(Currency c, int delta) {
	int newRubyAmount = c.getRubies() + delta;
	c.setRubies(newRubyAmount);

	getCurrencyDao().save(c);
    }

    public CurrencyDao getCurrencyDao() {
	return currencyDao;
    }
    public void setCurrencyDao(CurrencyDao currencyDao) {
	this.currencyDao = currencyDao;
    }

}