package com.lvl6.pictures.po;

import java.util.Date;

import javax.persistence.Entity;

import com.lvl6.gamesuite.common.po.BasePersistentObject;

@Entity
public class Currency extends BasePersistentObject {
  
  protected int tokens;
  
  protected Date lastTokenRefillTime;
  
  protected int rubies;

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
  
}