package com.lvl6.pictures.events.request;

import java.nio.ByteBuffer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.protobuf.ByteString;
import com.google.protobuf.InvalidProtocolBufferException;
import com.lvl6.pictures.eventprotos.CreateAccountEventProto.CreateAccountViaNoCredentialsRequestProto;
import com.lvl6.gamesuite.common.events.PreDatabaseRequestEvent;

public class CreateAccountViaNoCredentialsRequestEvent extends PreDatabaseRequestEvent {

  private static Logger log = LoggerFactory.getLogger(new Object() { }.getClass().getEnclosingClass());
  
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
      log.error("unexpected error: CreateAccountViaNoCredentialsRequestEvent. ", e);
    }
  }

  public CreateAccountViaNoCredentialsRequestProto getCreateAccountViaNoCredentialsRequestProto() {
    return createAccountViaNoCredentialsRequestProto;
  }
}
