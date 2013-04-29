package com.lvl6.gamesuite.common.controller.utils;

import java.util.Date;

import com.lvl6.gamesuite.common.noneventprotos.UserProto.BasicAuthorizedDeviceProto;
import com.lvl6.gamesuite.common.noneventprotos.UserProto.BasicAuthorizedDeviceProto.Builder;
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
    String facebookId = aUser.getFacebookId();
    
    bpb.setUserId(userId);
    if (null != nameStrangersSee && !nameStrangersSee.isEmpty()) {
      bpb.setNameStrangersSee(nameStrangersSee);
    }
    if (null != nameFriendsSee && !nameFriendsSee.isEmpty()) {
      bpb.setNameFriendsSee(nameFriendsSee);
    }
    if (null != email && !email.isEmpty()) {
      bpb.setEmail(email);
    }
    if (null != facebookId && !facebookId.isEmpty()) {
      bpb.setFacebookId(facebookId);
    }
    
    if (null != ad) {
      BasicAuthorizedDeviceProto badp = createBasicAuthorizedDeviceProto(ad, userId); 
      if (null != badp) {
        bpb.setBadp(badp);
      }
    }
    
    return bpb.build();
  }
  
  public BasicAuthorizedDeviceProto createBasicAuthorizedDeviceProto(AuthorizedDevice ad,
      String userId) {
    if (null != ad) {
      Builder bad = BasicAuthorizedDeviceProto.newBuilder();
      String udid = ad.getUdid();
      String loginToken = ad.getToken();
      Date expirationDate = ad.getExpires();
      
      bad.setBasicAuthorizedDeviceId(ad.getId());
      bad.setUserId(userId);
      bad.setLoginToken(loginToken);
      bad.setExpirationDate(expirationDate.getTime());
      bad.setUdid(udid);
      return bad.build();
    } else {
      return null;
    }
  }
}