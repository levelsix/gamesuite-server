package com.lvl6.pictures.events.response;
import java.nio.ByteBuffer;

import com.google.protobuf.ByteString;
import com.lvl6.gamesuite.common.events.NormalResponseEvent;
import com.lvl6.pictures.eventprotos.RetrieveNewQuestionsEventProto.RetrieveNewQuestionsResponseProto;
import com.lvl6.pictures.noneventprotos.PicturesEventProtocolProto.PicturesEventProtocolResponse;


public class RetrieveNewQuestionsResponseEvent extends NormalResponseEvent {

  private RetrieveNewQuestionsResponseProto retrieveNewQuestionsResponseProto;
  
  public RetrieveNewQuestionsResponseEvent(String playerId) {
    super(playerId);
    eventType = PicturesEventProtocolResponse.S_RETRIEVE_NEW_QUESTIONS_EVENT_VALUE;
  }
  
  @Override
  public int write(ByteBuffer bb) {
    ByteString b = retrieveNewQuestionsResponseProto.toByteString();
    b.copyTo(bb);
    return b.size();
  }
  
  public void setRetrieveNewQuestionsResponseProto(RetrieveNewQuestionsResponseProto retrieveNewQuestionsResponseProto) {
    this.retrieveNewQuestionsResponseProto = retrieveNewQuestionsResponseProto;
  }
  
}