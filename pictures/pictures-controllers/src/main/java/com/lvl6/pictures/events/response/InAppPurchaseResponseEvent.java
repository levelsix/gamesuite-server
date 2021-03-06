package com.lvl6.pictures.events.response;
import java.nio.ByteBuffer;

import com.google.protobuf.ByteString;
import com.lvl6.gamesuite.common.events.NormalResponseEvent;
import com.lvl6.gamesuite.common.noneventprotos.CommonEventProtocolProto.CommonEventProtocolResponse;
import com.lvl6.pictures.eventprotos.InAppPurchaseEventProto.InAppPurchaseResponseProto;


public class InAppPurchaseResponseEvent extends NormalResponseEvent {

  private InAppPurchaseResponseProto inAppPurchaseResponseProto;
  
  public InAppPurchaseResponseEvent(String playerId) {
    super(playerId);
    eventType = CommonEventProtocolResponse.S_IN_APP_PURCHASE_EVENT_VALUE;
  }
  
  @Override
  public int write(ByteBuffer bb) {
    ByteString b = inAppPurchaseResponseProto.toByteString();
    b.copyTo(bb);
    return b.size();
  }
  
  public void setInAppPurchaseResponseProto(InAppPurchaseResponseProto inAppPurchaseResponseProto) {
    this.inAppPurchaseResponseProto = inAppPurchaseResponseProto;
  }
  
}