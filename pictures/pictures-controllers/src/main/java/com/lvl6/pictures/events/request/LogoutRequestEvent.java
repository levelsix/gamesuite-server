package com.lvl6.pictures.events.request;

import java.nio.ByteBuffer;

import com.google.protobuf.ByteString;
import com.google.protobuf.InvalidProtocolBufferException;
import com.lvl6.gamesuite.common.events.RequestEvent;
import com.lvl6.pictures.eventprotos.LogoutEventProto.LogoutRequestProto;

public class LogoutRequestEvent extends RequestEvent {
  
  private LogoutRequestProto logoutRequestProto;

  @Override
  public void read(ByteBuffer bb) {
    try {
      logoutRequestProto = LogoutRequestProto.parseFrom(ByteString.copyFrom(bb));
      playerId = logoutRequestProto.getSender().getUserId();
    } catch (InvalidProtocolBufferException e) {
      e.printStackTrace();
    }
  }
  
  public LogoutRequestProto getLogoutRequestProto() {
    return logoutRequestProto;
  }
  
}