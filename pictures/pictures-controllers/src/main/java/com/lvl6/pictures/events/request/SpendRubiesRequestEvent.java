package com.lvl6.pictures.events.request;

import java.nio.ByteBuffer;

import com.google.protobuf.ByteString;
import com.google.protobuf.InvalidProtocolBufferException;
import com.lvl6.gamesuite.common.events.RequestEvent;
import com.lvl6.pictures.eventprotos.SpendRubiesEventProto.SpendRubiesRequestProto;

public class SpendRubiesRequestEvent extends RequestEvent {
  
  private SpendRubiesRequestProto spendRubiesRequestProto;

  @Override
  public void read(ByteBuffer bb) {
    try {
      spendRubiesRequestProto = SpendRubiesRequestProto.parseFrom(ByteString.copyFrom(bb));
      playerId = spendRubiesRequestProto.getSender().getUserId();
    } catch (InvalidProtocolBufferException e) {
      e.printStackTrace();
    }
  }
  
  public SpendRubiesRequestProto getSpendRubiesRequestProto() {
    return spendRubiesRequestProto;
  }
  
}