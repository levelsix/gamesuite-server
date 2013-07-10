package com.lvl6.pictures.services.inapppurchasehistory;

import java.util.Date;

import com.lvl6.pictures.dao.InAppPurchaseHistoryDao;

public interface InAppPurchaseHistoryService {
    
    public abstract boolean checkIfDuplicateTransaction(long transactionId);
    
    public abstract void recordNewInAppPurchase(String userId,
	    long transactionId, int rubiesBought, int tokensBought,
	    boolean isTokenRefill, Date purchaseDate);
    
    
    public abstract InAppPurchaseHistoryDao getInAppPurchaseHistoryDao();
    
    public abstract void setInAppPurchaseHistoryDao(
	    InAppPurchaseHistoryDao inAppPurchaseHistoryDao);
}