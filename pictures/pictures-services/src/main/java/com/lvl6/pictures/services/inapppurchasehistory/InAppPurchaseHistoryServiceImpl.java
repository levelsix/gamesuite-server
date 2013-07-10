package com.lvl6.pictures.services.inapppurchasehistory;

import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.lvl6.pictures.dao.InAppPurchaseHistoryDao;
import com.lvl6.pictures.po.InAppPurchaseHistory;

public class InAppPurchaseHistoryServiceImpl implements InAppPurchaseHistoryService {
    
    private static Logger log = LoggerFactory.getLogger(new Object() { }.getClass().getEnclosingClass());
    
    @Autowired
    protected InAppPurchaseHistoryDao inAppPurchaseHistoryDao;
    
    @Override
    public boolean checkIfDuplicateTransaction(long transactionId) {
	List<InAppPurchaseHistory> iaphList =
		inAppPurchaseHistoryDao.findByTransactionId(transactionId);
	
	if (null == iaphList || iaphList.isEmpty()) {
	    log.info("no transaction with id exists. id=" + transactionId);
	    return false;
	} else if (1 == iaphList.size()){
	    log.info("duplicate transaction with id exists. id=" + transactionId);
	} else {
	    log.error("more than one transaction with id exists. id=" +
		    transactionId + "\t num duplicates=" + iaphList.size());
	}
	return true;
    }
    
    @Override
    public void recordNewInAppPurchase(String userId, long transactionId,
	    int rubiesBought, int tokensBought, boolean isTokenRefill,
	    Date purchaseDate) {
	InAppPurchaseHistory iaph = new InAppPurchaseHistory();
	iaph.setUserId(userId);
	iaph.setTransactionId(transactionId);
	iaph.setTokens(tokensBought);
	iaph.setTokenRefill(isTokenRefill);
	iaph.setRubies(rubiesBought);
	iaph.setPurchaseDate(purchaseDate);
	inAppPurchaseHistoryDao.save(iaph);
    }

    
    @Override
    public InAppPurchaseHistoryDao getInAppPurchaseHistoryDao() {
        return inAppPurchaseHistoryDao;
    }

    @Override
    public void setInAppPurchaseHistoryDao(
    	InAppPurchaseHistoryDao inAppPurchaseHistoryDao) {
        this.inAppPurchaseHistoryDao = inAppPurchaseHistoryDao;
    }
    
    
}