package com.lvl6.pictures.events.request;

import java.nio.ByteBuffer;

import com.google.protobuf.ByteString;
import com.google.protobuf.InvalidProtocolBufferException;
import com.lvl6.gamesuite.common.events.RequestEvent;
import com.lvl6.pictures.eventprotos.RefillTokensByWaitingEventProto.RefillTokensByWaitingRequestProto;

public class RefillTokensByWaitingRequestEvent extends RequestEvent {
  
  private RefillTokensByWaitingRequestProto refillTokensByWaitingRequestProto;

  @Override
  public void read(ByteBuffer bb) {
    try {
      refillTokensByWaitingRequestProto = RefillTokensByWaitingRequestProto.parseFrom(ByteString.copyFrom(bb));
      playerId = refillTokensByWaitingRequestProto.getSender().getUserId();
    } catch (InvalidProtocolBufferException e) {
      e.printStackTrace();
    }
  }
  
  public RefillTokensByWaitingRequestProto getRefillTokensByWaitingRequestProto() {
    return refillTokensByWaitingRequestProto;
  }
  
}