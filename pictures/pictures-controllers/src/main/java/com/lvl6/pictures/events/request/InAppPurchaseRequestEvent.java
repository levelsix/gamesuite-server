package com.lvl6.pictures.events.request;

import java.nio.ByteBuffer;

import com.google.protobuf.ByteString;
import com.google.protobuf.InvalidProtocolBufferException;
import com.lvl6.gamesuite.common.events.RequestEvent;
import com.lvl6.pictures.eventprotos.InAppPurchaseEventProto.InAppPurchaseRequestProto;

public class InAppPurchaseRequestEvent extends RequestEvent {
  
  private InAppPurchaseRequestProto inAppPurchaseRequestProto;

  @Override
  public void read(ByteBuffer bb) {
    try {
      inAppPurchaseRequestProto = InAppPurchaseRequestProto.parseFrom(ByteString.copyFrom(bb));
      playerId = inAppPurchaseRequestProto.getSender().getUserId();
    } catch (InvalidProtocolBufferException e) {
      e.printStackTrace();
    }
  }
  
  public InAppPurchaseRequestProto getInAppPurchaseRequestProto() {
    return inAppPurchaseRequestProto;
  }
  
}