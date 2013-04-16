package com.lvl6.gamesuite.common.events.request;

import java.nio.ByteBuffer;

import com.google.protobuf.ByteString;
import com.google.protobuf.InvalidProtocolBufferException;
import com.lvl6.gamesuite.common.eventprotos.CreateAccountEventProto.CreateAccountViaFacebookRequestProto;
import com.lvl6.gamesuite.common.events.PreDatabaseRequestEvent;

public class CreateAccountViaFacebookRequestEvent extends PreDatabaseRequestEvent {

  private CreateAccountViaFacebookRequestProto createAccountViaFacebookRequestProto;
  
  /**
   * read the event from the given ByteBuffer to populate this event
   */
  public void read(ByteBuffer buff) {
    try {
      createAccountViaFacebookRequestProto = CreateAccountViaFacebookRequestProto.parseFrom(ByteString.copyFrom(buff));
      
      //Player id is -1 since it won't be initialized yet.
      playerId = -1;
      
      udid = createAccountViaFacebookRequestProto.getUdid();
    } catch (InvalidProtocolBufferException e) {
      e.printStackTrace();
    }
  }

  public CreateAccountViaFacebookRequestProto getcreateAccountViaFacebookRequestProto() {
    return createAccountViaFacebookRequestProto;
  }
}
