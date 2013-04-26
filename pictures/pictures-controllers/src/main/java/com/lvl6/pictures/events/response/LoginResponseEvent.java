package com.lvl6.pictures.events.response;

import java.nio.ByteBuffer;

import com.google.protobuf.ByteString;
import com.lvl6.gamesuite.common.events.NormalResponseEvent;
import com.lvl6.pictures.eventprotos.LoginEventProto.LoginResponseProto;
import com.lvl6.pictures.noneventprotos.PicturesEventProtocolProto.PicturesEventProtocolResponse;

public class LoginResponseEvent extends NormalResponseEvent {
  private LoginResponseProto loginResponseProto;
  
  public LoginResponseEvent(String playerId) {
    super(playerId);
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