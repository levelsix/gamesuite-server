package com.lvl6.gamesuite.common.controller.utils;

import com.lvl6.gamesuite.common.noneventprotos.UserProto.BasicUserProto;

public interface CreateNoneventProtoUtils {
  
  public BasicUserProto createBasicUserProto (String userId, String name, String udid);
}