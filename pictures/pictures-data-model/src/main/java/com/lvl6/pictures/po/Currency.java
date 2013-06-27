package com.lvl6.pictures.po;

import java.util.Date;

import javax.persistence.Entity;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.lvl6.gamesuite.common.po.BasePersistentObject;

@Entity
public class Currency extends BasePersistentObject {

    private static final long serialVersionUID = -3736629418064204471L;

    //used to play games against opponents
    protected int tokens;

    protected Date lastTokenRefillTime;

    //used to purchase power ups
    protected int rubies;

    protected String userId;



    public int getTokens() {
	return tokens;
    }

    public void setTokens(int tokens) {
	this.tokens = tokens;
    }

    public Date getLastTokenRefillTime() {
	return lastTokenRefillTime;
    }

    public void setLastTokenRefillTime(Date lastTokenRefillTime) {
	this.lastTokenRefillTime = lastTokenRefillTime;
    }

    public int getRubies() {
	return rubies;
    }

    public void setRubies(int rubies) {
	this.rubies = rubies;
    }

    public String getUserId() {
	return userId;
    }

    public void setUserId(String userId) {
	this.userId = userId;
    }

    @Override
    public String toString() {
	return ToStringBuilder.reflectionToString(this);
    }
}