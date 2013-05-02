package com.lvl6.pictures.events.response;

import java.nio.ByteBuffer;

import com.google.protobuf.ByteString;
import com.lvl6.pictures.eventprotos.CreateAccountEventProto.CreateAccountResponseProto;
import com.lvl6.gamesuite.common.events.PreDatabaseResponseEvent;
import com.lvl6.gamesuite.common.noneventprotos.CommonEventProtocolProto.CommonEventProtocolResponse;

public class CreateAccountViaEmailResponseEvent extends PreDatabaseResponseEvent{

  private CreateAccountResponseProto createAccountResponseProto;
  
  public CreateAccountViaEmailResponseEvent(String udid) {
    super(udid);
    eventType = CommonEventProtocolResponse.S_CREATE_ACCOUNT_VIA_EMAIL_EVENT_VALUE;
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

  public void setCreateAccountResponseProto(CreateAccountResponseProto createAccountResponseProto) {
    this.createAccountResponseProto = createAccountResponseProto;
  }
  
}
