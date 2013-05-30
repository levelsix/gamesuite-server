package com.lvl6.pictures.events.request;

import java.nio.ByteBuffer;

import com.google.protobuf.ByteString;
import com.google.protobuf.InvalidProtocolBufferException;
import com.lvl6.gamesuite.common.events.RequestEvent;
import com.lvl6.pictures.eventprotos.RetrieveNewQuestionsEventProto.RetrieveNewQuestionsRequestProto;

public class RetrieveNewQuestionsRequestEvent extends RequestEvent {
  
  private RetrieveNewQuestionsRequestProto retrieveNewQuestionsRequestProto;

  @Override
  public void read(ByteBuffer bb) {
    try {
      retrieveNewQuestionsRequestProto = RetrieveNewQuestionsRequestProto.parseFrom(ByteString.copyFrom(bb));
      playerId = retrieveNewQuestionsRequestProto.getSender().getUserId();
    } catch (InvalidProtocolBufferException e) {
      e.printStackTrace();
    }
  }
  
  public RetrieveNewQuestionsRequestProto getRetrieveNewQuestionsRequestProto() {
    return retrieveNewQuestionsRequestProto;
  }
  
}