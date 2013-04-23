package com.lvl6.gamesuite.common.controller.utils;

import com.lvl6.gamesuite.common.noneventprotos.UserProto.BasicUserProto;

public class CreateNoneventProtoUtilsImpl implements CreateNoneventProtoUtils {
  
  public BasicUserProto createBasicUserProto (String userId, String name, String udid) {
    BasicUserProto.Builder bpb = BasicUserProto.newBuilder();
    
    bpb.setUserId(userId);
    if (null != name) {
      bpb.setName(name);
    }
    if (null != udid) { 
      bpb.setUdid(udid);
    }
    
    return bpb.build();
  }
}