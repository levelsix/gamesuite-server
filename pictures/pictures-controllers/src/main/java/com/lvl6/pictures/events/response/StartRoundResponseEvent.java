package com.lvl6.pictures.events.response;
import java.nio.ByteBuffer;

import com.google.protobuf.ByteString;
import com.lvl6.gamesuite.common.events.NormalResponseEvent;
import com.lvl6.pictures.eventprotos.StartRoundEventProto.StartRoundResponseProto;
import com.lvl6.pictures.noneventprotos.PicturesEventProtocolProto.PicturesEventProtocolResponse;


public class StartRoundResponseEvent extends NormalResponseEvent {

  private StartRoundResponseProto startRoundResponseProto;
  
  public StartRoundResponseEvent(String playerId) {
    super(playerId);
    eventType = PicturesEventProtocolResponse.S_START_ROUND_EVENT_VALUE;
  }
  
  @Override
  public int write(ByteBuffer bb) {
    ByteString b = startRoundResponseProto.toByteString();
    b.copyTo(bb);
    return b.size();
  }
  
  public void setStartRoundResponseProto(StartRoundResponseProto startRoundResponseProto) {
    this.startRoundResponseProto = startRoundResponseProto;
  }
  
}