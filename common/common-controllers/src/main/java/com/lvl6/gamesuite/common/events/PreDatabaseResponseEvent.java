package com.lvl6.gamesuite.common.events;

public abstract class PreDatabaseResponseEvent extends ResponseEvent{
  protected String udid;
  
  public PreDatabaseResponseEvent(String udid) {
    this.udid = udid;
  }

  public String getUdid() {
    return udid;
  }
}
