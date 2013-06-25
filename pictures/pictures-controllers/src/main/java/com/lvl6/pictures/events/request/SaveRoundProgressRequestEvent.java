package com.lvl6.pictures.events.request;

import java.nio.ByteBuffer;

import com.google.protobuf.ByteString;
import com.google.protobuf.InvalidProtocolBufferException;
import com.lvl6.gamesuite.common.events.RequestEvent;
import com.lvl6.pictures.eventprotos.SaveRoundProgressEventProto.SaveRoundProgressRequestProto;

public class SaveRoundProgressRequestEvent extends RequestEvent {
  
  private SaveRoundProgressRequestProto saveRoundProgressRequestProto;

  @Override
  public void read(ByteBuffer bb) {
    try {
      saveRoundProgressRequestProto = SaveRoundProgressRequestProto.parseFrom(ByteString.copyFrom(bb));
      playerId = saveRoundProgressRequestProto.getSender().getUserId();
    } catch (InvalidProtocolBufferException e) {
      e.printStackTrace();
    }
  }
  
  public SaveRoundProgressRequestProto getSaveRoundProgressRequestProto() {
    return saveRoundProgressRequestProto;
  }
  
}