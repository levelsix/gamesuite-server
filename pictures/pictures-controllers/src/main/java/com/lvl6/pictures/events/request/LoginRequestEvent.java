package com.lvl6.pictures.events.request;

import java.nio.ByteBuffer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.protobuf.ByteString;
import com.google.protobuf.InvalidProtocolBufferException;
import com.lvl6.gamesuite.common.events.PreDatabaseRequestEvent;
import com.lvl6.pictures.eventprotos.LoginEventProto.LoginRequestProto;

public class LoginRequestEvent extends PreDatabaseRequestEvent {

  private static Logger log = LoggerFactory.getLogger(new Object() { }.getClass().getEnclosingClass());
  
  private LoginRequestProto loginRequestProto;
  
  /**
   * read the event from the given ByteBuffer to populate this event
   */
  public void read(ByteBuffer buff) {
    try {
      loginRequestProto = LoginRequestProto.parseFrom(ByteString.copyFrom(buff));
      
      //Player id is "" since it won't be initialized yet.
      playerId = "";//loginRequestProto.getSender().getUserId();
      
      udid = loginRequestProto.getSender().getBadp().getUdid();
    } catch (InvalidProtocolBufferException e) {
      log.error("unexpected error: LoginRequestEvent. ", e);
    }
  }

  public LoginRequestProto getLoginRequestProto() {
    return loginRequestProto;
  }
}
