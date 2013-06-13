package com.lvl6.pictures.events.response;
import java.nio.ByteBuffer;

import com.google.protobuf.ByteString;
import com.lvl6.gamesuite.common.events.NormalResponseEvent;
import com.lvl6.pictures.eventprotos.CompletedRoundEventProto.CompletedRoundResponseProto;
import com.lvl6.pictures.noneventprotos.PicturesEventProtocolProto.PicturesEventProtocolResponse;


public class CompletedRoundResponseEvent extends NormalResponseEvent {

  private CompletedRoundResponseProto completedRoundResponseProto;
  
  public CompletedRoundResponseEvent(String playerId) {
    super(playerId);
    eventType = PicturesEventProtocolResponse.S_COMPLETED_ROUND_EVENT_VALUE;
  }
  
  @Override
  public int write(ByteBuffer bb) {
    ByteString b = completedRoundResponseProto.toByteString();
    b.copyTo(bb);
    return b.size();
  }
  
  public void setCompletedRoundResponseProto(CompletedRoundResponseProto completedRoundResponseProto) {
    this.completedRoundResponseProto = completedRoundResponseProto;
  }
  
}