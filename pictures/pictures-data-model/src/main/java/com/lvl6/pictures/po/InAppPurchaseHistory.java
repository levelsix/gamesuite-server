package com.lvl6.pictures.po;

import java.util.Date;

import javax.persistence.Entity;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.lvl6.gamesuite.common.po.BasePersistentObject;

@Entity
public class InAppPurchaseHistory extends BasePersistentObject {

    private static final long serialVersionUID = -6863948949715625090L;

    protected String userId;
    
    protected long transactionId;

    //used to play games against opponents
    protected int tokens;
    
    protected boolean isTokenRefill;
    
    //used to purchase power ups
    protected int rubies;

    protected Date purchaseDate;
    

    public String getUserId() {
	return userId;
    }

    public void setUserId(String userId) {
	this.userId = userId;
    }

    public long getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(long transactionId) {
        this.transactionId = transactionId;
    }

    public int getTokens() {
	return tokens;
    }

    public void setTokens(int tokens) {
	this.tokens = tokens;
    }

    public boolean isTokenRefill() {
        return isTokenRefill;
    }

    public void setTokenRefill(boolean isTokenRefill) {
        this.isTokenRefill = isTokenRefill;
    }

    public int getRubies() {
	return rubies;
    }

    public void setRubies(int rubies) {
	this.rubies = rubies;
    }

    public Date getPurchaseDate() {
        return purchaseDate;
    }

    public void setPurchaseDate(Date purchaseDate) {
        this.purchaseDate = purchaseDate;
    }

    @Override
    public String toString() {
	return ToStringBuilder.reflectionToString(this);
    }
}