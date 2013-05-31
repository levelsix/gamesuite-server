package com.lvl6.pictures.events.response;
import java.nio.ByteBuffer;

import com.google.protobuf.ByteString;
import com.lvl6.gamesuite.common.events.NormalResponseEvent;
import com.lvl6.pictures.eventprotos.SpendRubiesEventProto.SpendRubiesResponseProto;
import com.lvl6.pictures.noneventprotos.PicturesEventProtocolProto.PicturesEventProtocolResponse;


public class SpendRubiesResponseEvent extends NormalResponseEvent {

  private SpendRubiesResponseProto spendRubiesResponseProto;
  
  public SpendRubiesResponseEvent(String playerId) {
    super(playerId);
    eventType = PicturesEventProtocolResponse.S_SPEND_RUBIES_EVENT_VALUE;
  }
  
  @Override
  public int write(ByteBuffer bb) {
    ByteString b = spendRubiesResponseProto.toByteString();
    b.copyTo(bb);
    return b.size();
  }
  
  public void setSpendRubiesResponseProto(SpendRubiesResponseProto spendRubiesResponseProto) {
    this.spendRubiesResponseProto = spendRubiesResponseProto;
  }
  
}