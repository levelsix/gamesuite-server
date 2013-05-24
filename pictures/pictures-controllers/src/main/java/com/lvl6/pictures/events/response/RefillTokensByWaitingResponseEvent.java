package com.lvl6.pictures.events.response;
import java.nio.ByteBuffer;

import com.google.protobuf.ByteString;
import com.lvl6.gamesuite.common.events.NormalResponseEvent;
import com.lvl6.pictures.eventprotos.RefillTokensByWaitingEventProto.RefillTokensByWaitingResponseProto;
import com.lvl6.pictures.noneventprotos.PicturesEventProtocolProto.PicturesEventProtocolResponse;


public class RefillTokensByWaitingResponseEvent extends NormalResponseEvent {

  private RefillTokensByWaitingResponseProto refillTokensByWaitingResponseProto;
  
  public RefillTokensByWaitingResponseEvent(String playerId) {
    super(playerId);
    eventType = PicturesEventProtocolResponse.S_REFILL_TOKENS_BY_WAITING_EVENT_VALUE;
  }
  
  @Override
  public int write(ByteBuffer bb) {
    ByteString b = refillTokensByWaitingResponseProto.toByteString();
    b.copyTo(bb);
    return b.size();
  }
  
  public void setRefillTokensByWaitingResponseProto(RefillTokensByWaitingResponseProto refillTokensByWaitingResponseProto) {
    this.refillTokensByWaitingResponseProto = refillTokensByWaitingResponseProto;
  }
  
}