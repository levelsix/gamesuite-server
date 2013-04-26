package com.lvl6.pictures.events.response;

import java.nio.ByteBuffer;

import com.google.protobuf.ByteString;
import com.lvl6.gamesuite.common.events.PreDatabaseResponseEvent;
import com.lvl6.pictures.eventprotos.LoginEventProto.LoginResponseProto;
import com.lvl6.pictures.noneventprotos.PicturesEventProtocolProto.PicturesEventProtocolResponse;

public class LoginResponseEvent extends PreDatabaseResponseEvent {
  private LoginResponseProto loginResponseProto;
  
  public LoginResponseEvent(String udid) {
    super(udid);
    eventType = PicturesEventProtocolResponse.S_LOGIN_EVENT_VALUE;
  }
  
  @Override
  public int write(ByteBuffer bb) {
    ByteString b = loginResponseProto.toByteString();
    b.copyTo(bb);
    return b.size();
  }
  
  public void setLoginResponseProto(LoginResponseProto loginResponseProto) {
    this.loginResponseProto = loginResponseProto;
  }
  
}