package com.lvl6.pictures.events.response;
import java.nio.ByteBuffer;

import com.google.protobuf.ByteString;
import com.lvl6.gamesuite.common.events.NormalResponseEvent;
import com.lvl6.pictures.eventprotos.SaveRoundProgressEventProto.SaveRoundProgressResponseProto;
import com.lvl6.pictures.noneventprotos.PicturesEventProtocolProto.PicturesEventProtocolResponse;


public class SaveRoundProgressResponseEvent extends NormalResponseEvent {

  private SaveRoundProgressResponseProto saveRoundProgressResponseProto;
  
  public SaveRoundProgressResponseEvent(String playerId) {
    super(playerId);
    eventType = PicturesEventProtocolResponse.S_SAVE_ROUND_PROGRESS_EVENT_VALUE;
  }
  
  @Override
  public int write(ByteBuffer bb) {
    ByteString b = saveRoundProgressResponseProto.toByteString();
    b.copyTo(bb);
    return b.size();
  }
  
  public void setSaveRoundProgressResponseProto(SaveRoundProgressResponseProto saveRoundProgressResponseProto) {
    this.saveRoundProgressResponseProto = saveRoundProgressResponseProto;
  }
  
}