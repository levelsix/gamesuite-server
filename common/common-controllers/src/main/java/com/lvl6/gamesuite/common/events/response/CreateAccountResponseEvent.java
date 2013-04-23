package com.lvl6.gamesuite.common.events.response;

import java.nio.ByteBuffer;

import com.google.protobuf.ByteString;
import com.lvl6.gamesuite.common.eventprotos.CreateAccountEventProto.CreateAccountResponseProto;
import com.lvl6.gamesuite.common.events.PreDatabaseResponseEvent;
import com.lvl6.gamesuite.common.noneventprotos.CommonEventProtocolProto.CommonEventProtocolResponse;

public class CreateAccountResponseEvent extends PreDatabaseResponseEvent{

  private CreateAccountResponseProto createAccountResponseProto;
  
  public CreateAccountResponseEvent(String udid) {
    super(udid);
    eventType = CommonEventProtocolResponse.S_CREATE_ACCOUNT_EVENT.getNumber();
  }
  
  /** 
   * write the event to the given ByteBuffer
   * 
   * note we are using 1.4 ByteBuffers for both client and server
   * depending on the deployment you may need to support older java
   * versions on the client and use old-style socket input/output streams
   */
  public int write(ByteBuffer buff) {
    ByteString b = createAccountResponseProto.toByteString();
    b.copyTo(buff);
    return b.size();
  }

  public void setUserCreateResponseProto(CreateAccountResponseProto UserCreateResponseProto) {
    this.createAccountResponseProto = UserCreateResponseProto;
  }
  
}
