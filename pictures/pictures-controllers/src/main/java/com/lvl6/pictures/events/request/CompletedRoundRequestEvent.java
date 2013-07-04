package com.lvl6.pictures.events.request;

import java.nio.ByteBuffer;

import com.google.protobuf.ByteString;
import com.google.protobuf.InvalidProtocolBufferException;
import com.lvl6.gamesuite.common.events.RequestEvent;
import com.lvl6.pictures.eventprotos.CompletedRoundEventProto.CompletedRoundRequestProto;

public class CompletedRoundRequestEvent extends RequestEvent {
  
  private CompletedRoundRequestProto completedRoundRequestProto;

  @Override
  public void read(ByteBuffer bb) {
    try {
      completedRoundRequestProto = CompletedRoundRequestProto.parseFrom(ByteString.copyFrom(bb));
      playerId = completedRoundRequestProto.getSender().getUserId();
    } catch (InvalidProtocolBufferException e) {
      e.printStackTrace();
    }
  }
  
  public CompletedRoundRequestProto getCompletedRoundRequestProto() {
    return completedRoundRequestProto;
  }

  public void setCompletedRoundRequestProto(
	  CompletedRoundRequestProto completedRoundRequestProto) {
      this.completedRoundRequestProto = completedRoundRequestProto;
  }

}