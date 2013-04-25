package com.lvl6.gamesuite.common.events.request;

import java.nio.ByteBuffer;

import com.google.protobuf.ByteString;
import com.google.protobuf.InvalidProtocolBufferException;
import com.lvl6.gamesuite.common.eventprotos.CreateAccountEventProto.CreateAccountViaNoCredentialsRequestProto;
import com.lvl6.gamesuite.common.events.PreDatabaseRequestEvent;

public class CreateAccountViaNoCredentialsRequestEvent extends PreDatabaseRequestEvent {

  private CreateAccountViaNoCredentialsRequestProto createAccountViaNoCredentialsRequestProto;
  
  /**
   * read the event from the given ByteBuffer to populate this event
   */
  public void read(ByteBuffer buff) {
    try {
      createAccountViaNoCredentialsRequestProto = CreateAccountViaNoCredentialsRequestProto.parseFrom(ByteString.copyFrom(buff));
      
      //Player id is "" since it won't be initialized yet.
      playerId = "";
      
      udid = createAccountViaNoCredentialsRequestProto.getUdid();
    } catch (InvalidProtocolBufferException e) {
      e.printStackTrace();
    }
  }

  public CreateAccountViaNoCredentialsRequestProto getcreateAccountViaNoCredentialsRequestProto() {
    return createAccountViaNoCredentialsRequestProto;
  }
}
