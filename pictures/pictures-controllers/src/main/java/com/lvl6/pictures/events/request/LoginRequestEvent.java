package com.lvl6.pictures.events.request;

import java.nio.ByteBuffer;

import com.google.protobuf.ByteString;
import com.google.protobuf.InvalidProtocolBufferException;
import com.lvl6.gamesuite.common.events.RequestEvent;
import com.lvl6.pictures.eventprotos.LoginEventProto.LoginRequestProto;

public class LoginRequestEvent extends RequestEvent {

  private LoginRequestProto loginRequestProto;
  
  /**
   * read the event from the given ByteBuffer to populate this event
   */
  public void read(ByteBuffer buff) {
    try {
      loginRequestProto = LoginRequestProto.parseFrom(ByteString.copyFrom(buff));
      playerId = loginRequestProto.getSender().getUserId();
    } catch (InvalidProtocolBufferException e) {
      e.printStackTrace();
    }
  }

  public LoginRequestProto getLoginRequestProto() {
    return loginRequestProto;
  }
}
