package com.lvl6.gamesuite.common.controller.utils;

import java.util.Date;

import com.lvl6.gamesuite.common.noneventprotos.UserProto.BasicAuthorizedDevice;
import com.lvl6.gamesuite.common.noneventprotos.UserProto.BasicAuthorizedDevice.Builder;
import com.lvl6.gamesuite.common.noneventprotos.UserProto.BasicUserProto;
import com.lvl6.gamesuite.common.po.AuthorizedDevice;
import com.lvl6.gamesuite.common.po.User;

public class CreateNoneventProtoUtilsImpl implements CreateNoneventProtoUtils {
  
  public BasicUserProto createBasicUserProto (User aUser, AuthorizedDevice ad) {
    BasicUserProto.Builder bpb = BasicUserProto.newBuilder();
    String userId = aUser.getId();
    String nameStrangersSee = aUser.getNameStrangersSee();
    String nameFriendsSee = aUser.getNameFriendsSee();
    String email = aUser.getEmail();

    bpb.setUserId(userId);
    if (null != nameStrangersSee) {
      bpb.setNameStrangersSee(nameStrangersSee);
    }
    if (null != nameFriendsSee) {
      bpb.setNameFriendsSee(nameFriendsSee);
    }
    if (null != email) {
      bpb.setEmail(email);
    }
    
    if (null != ad) {
      Builder bad = BasicAuthorizedDevice.newBuilder();
      String udid = ad.getUdid();
      String loginToken = ad.getToken();
      Date expirationDate = ad.getExpires();
      bad.setUdid(udid);
      if (null != loginToken) {
        bad.setLoginToken(loginToken);
      }
      if (null != expirationDate) {
        bad.setExpirationDate(expirationDate.getTime());
      }
      if (null != udid) {
        bad.setUdid(udid);
      }

      bpb.setBasicAuthorizedDevice(bad.build());
    }
    return bpb.build();
  }
}