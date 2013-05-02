package com.lvl6.pictures.controller.utils;

import com.lvl6.pictures.noneventprotos.UserProto.BasicUserProto;
import com.lvl6.gamesuite.common.po.AuthorizedDevice;
import com.lvl6.gamesuite.common.po.User;

public interface CreateNoneventProtoUtils {
  
  public abstract BasicUserProto createBasicUserProto (User aUser, AuthorizedDevice ad);
  
}