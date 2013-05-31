package com.lvl6.pictures.events.response;
import java.nio.ByteBuffer;

import com.google.protobuf.ByteString;
import com.lvl6.gamesuite.common.events.NormalResponseEvent;
import com.lvl6.pictures.eventprotos.SearchForUserEventProto.SearchForUserResponseProto;
import com.lvl6.pictures.noneventprotos.PicturesEventProtocolProto.PicturesEventProtocolResponse;


public class SearchForUserResponseEvent extends NormalResponseEvent {

  private SearchForUserResponseProto searchForUserResponseProto;
  
  public SearchForUserResponseEvent(String playerId) {
    super(playerId);
    eventType = PicturesEventProtocolResponse.S_SEARCH_FOR_USER_EVENT_VALUE;
  }
  
  @Override
  public int write(ByteBuffer bb) {
    ByteString b = searchForUserResponseProto.toByteString();
    b.copyTo(bb);
    return b.size();
  }
  
  public void setSearchForUserResponseProto(SearchForUserResponseProto searchForUserResponseProto) {
    this.searchForUserResponseProto = searchForUserResponseProto;
  }
  
}