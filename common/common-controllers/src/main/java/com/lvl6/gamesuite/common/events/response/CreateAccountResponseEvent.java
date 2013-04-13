//package com.lvl6.gamesuite.common.events.response;
//
//import java.nio.ByteBuffer;
//
//import com.google.protobuf.ByteString;
//import com.lvl6.gamesuite.common.events.ResponseEvent;
////import com.lvl6.proto.EventProto.ApproveOrRejectRequestToJoinClanResponseProto;
////import com.lvl6.proto.ProtocolsProto.EventProtocolResponse;
//
//public class CreateAccountResponseEvent extends ResponseEvent {
//
//  private CreateAccountResponseProto createAccountResponseProto;
//  
//  public CreateAccountResponseEvent(int playerId){
//    super(playerId);
//    eventType = null;//EventProtocolResponse.S_APPROVE_OR_REJECT_REQUEST_TO_JOIN_CLAN_EVENT;
//  }
//  
//  @Override
//  public int write(ByteBuffer bb) {
//    ByteString b = createAccountResponseProto.toByteString();
//    b.copyTo(bb);
//    return b.size();
//  }
//
//  public void setCreateAccountResponseProto(CreateAccountResponseProto createAccountResponseProto) {
//    this.createAccountResponseProto = createAccountResponseProto;
//  }
//
//}
