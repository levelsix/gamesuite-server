package com.lvl6.pictures.events.request;

import java.nio.ByteBuffer;

import com.google.protobuf.ByteString;
import com.google.protobuf.InvalidProtocolBufferException;
import com.lvl6.gamesuite.common.events.RequestEvent;
import com.lvl6.pictures.eventprotos.SearchForUserEventProto.SearchForUserRequestProto;

public class SearchForUserRequestEvent extends RequestEvent {
  
  private SearchForUserRequestProto searchForUserRequestProto;

  @Override
  public void read(ByteBuffer bb) {
    try {
      searchForUserRequestProto = SearchForUserRequestProto.parseFrom(ByteString.copyFrom(bb));
      playerId = searchForUserRequestProto.getSender().getUserId();
    } catch (InvalidProtocolBufferException e) {
      e.printStackTrace();
    }
  }
  
  public SearchForUserRequestProto getSearchForUserRequestProto() {
    return searchForUserRequestProto;
  }
  
}