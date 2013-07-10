package com.lvl6.pictures.events.response;
import java.nio.ByteBuffer;

import com.google.protobuf.ByteString;
import com.lvl6.gamesuite.common.events.NormalResponseEvent;
import com.lvl6.pictures.eventprotos.UpdateClientProto.UpdateClientCurrencyResponseProto;
import com.lvl6.pictures.noneventprotos.PicturesEventProtocolProto.PicturesEventProtocolResponse;


public class UpdateClientCurrencyResponseEvent extends NormalResponseEvent {

  private UpdateClientCurrencyResponseProto updateClientCurrencyResponseProto;
  
  public UpdateClientCurrencyResponseEvent(String playerId) {
    super(playerId);
    eventType = PicturesEventProtocolResponse.S_SPEND_RUBIES_EVENT_VALUE;
  }
  
  @Override
  public int write(ByteBuffer bb) {
    ByteString b = updateClientCurrencyResponseProto.toByteString();
    b.copyTo(bb);
    return b.size();
  }
  
  public void setUpdateClientResponseProto(
	  UpdateClientCurrencyResponseProto updateClientCurrencyResponseProto) {
    this.updateClientCurrencyResponseProto = updateClientCurrencyResponseProto;
  }
  
}