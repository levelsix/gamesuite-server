package com.lvl6.gamesuite.common.events.request;

import java.nio.ByteBuffer;

import com.google.protobuf.ByteString;
import com.google.protobuf.InvalidProtocolBufferException;
import com.lvl6.gamesuite.common.eventprotos.CreateAccountEventProto.CreateAccountViaEmailRequestProto;
import com.lvl6.gamesuite.common.events.PreDatabaseRequestEvent;

public class CreateAccountViaEmailRequestEvent extends PreDatabaseRequestEvent {

  private CreateAccountViaEmailRequestProto createAccountViaEmailRequestProto;
  
  /**
   * read the event from the given ByteBuffer to populate this event
   */
  public void read(ByteBuffer buff) {
    try {
      createAccountViaEmailRequestProto = CreateAccountViaEmailRequestProto.parseFrom(ByteString.copyFrom(buff));
      
      //Player id is "" since it won't be initialized yet.
      playerId = "";
      
      udid = createAccountViaEmailRequestProto.getUdid();
    } catch (InvalidProtocolBufferException e) {
      e.printStackTrace();
    }
  }

  public CreateAccountViaEmailRequestProto getcreateAccountViaEmailRequestProto() {
    return createAccountViaEmailRequestProto;
  }
}
