package com.lvl6.pictures.controller.utils;

import java.util.Date;
import java.util.Set;

import com.lvl6.gamesuite.common.po.AuthorizedDevice;
import com.lvl6.gamesuite.common.po.User;
import com.lvl6.pictures.noneventprotos.TriviaQuestionFormatProto.MultipleChoiceAnswerProto;
import com.lvl6.pictures.noneventprotos.TriviaQuestionFormatProto.MultipleChoiceQuestionProto;
import com.lvl6.pictures.noneventprotos.TriviaQuestionFormatProto.PictureQuestionProto;
import com.lvl6.pictures.noneventprotos.TriviaQuestionFormatProto.QuestionProto;
import com.lvl6.pictures.noneventprotos.UserProto.BasicAuthorizedDeviceProto;
import com.lvl6.pictures.noneventprotos.UserProto.BasicAuthorizedDeviceProto.Builder;
import com.lvl6.pictures.noneventprotos.UserProto.BasicUserProto;
import com.lvl6.pictures.po.AnswerType;
import com.lvl6.pictures.po.MultipleChoiceAnswer;
import com.lvl6.pictures.po.MultipleChoiceQuestion;
import com.lvl6.pictures.po.PicturesQuestionWithTextAnswer;

public class CreateNoneventProtoUtilsImpl implements CreateNoneventProtoUtils {
  
  @Override
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
  
  @Override
  public BasicAuthorizedDeviceProto createBasicAuthorizedDeviceProto(AuthorizedDevice ad,
      String userId) {
    if (null != ad) {
      Builder bad = BasicAuthorizedDeviceProto.newBuilder();
      String udid = ad.getUdid();
      String loginToken = ad.getToken();
      Date expirationDate = ad.getExpires();
      String deviceId = ad.getDeviceId();
      
      bad.setBasicAuthorizedDeviceId(ad.getId());
      bad.setUserId(userId);
      bad.setLoginToken(loginToken);
      bad.setExpirationDate(expirationDate.getTime());
      bad.setUdid(udid);
      bad.setDeviceId(deviceId);
      return bad.build();
    } else {
      return null;
    }
  }
  
  @Override
  public QuestionProto createQuestionProto(MultipleChoiceQuestion mcq, PicturesQuestionWithTextAnswer pqwta) {
    QuestionProto.Builder qpb = QuestionProto.newBuilder();
    if (null == mcq) {
      PictureQuestionProto pqp = createPictureQuestionProto(pqwta);
      qpb.setPictures(pqp);
    } else {
      MultipleChoiceQuestionProto mcqp = createMultipleChoiceQuestionProto(mcq);
      qpb.setMultipleChoice(mcqp);
    }
    
    return qpb.build();
  }
  
  @Override
  public MultipleChoiceQuestionProto createMultipleChoiceQuestionProto(MultipleChoiceQuestion mcq) {
    MultipleChoiceQuestionProto.Builder mcqpb = MultipleChoiceQuestionProto.newBuilder();
    String mcqId = mcq.getId();
    String question = mcq.getQuestion();
    Set<MultipleChoiceAnswer> mcaSet = mcq.getAnswers();
    
    mcqpb.setId(mcqId);
    mcqpb.setQuestion(question);
    for (MultipleChoiceAnswer mca : mcaSet) {
      if (mca.isCorrect()) {
        String answerId = mca.getId();
        mcqpb.setAnswerId(answerId);
      }
      MultipleChoiceAnswerProto mcap = createMultipleChoiceAnswerProto(mca);
      mcqpb.addAnswers(mcap);
    }
    return mcqpb.build();
  }
  
  @Override
  public MultipleChoiceAnswerProto createMultipleChoiceAnswerProto(MultipleChoiceAnswer mca) {
    MultipleChoiceAnswerProto.Builder mcapb = MultipleChoiceAnswerProto.newBuilder();
    String id = mca.getId();
    String answer = mca.getAnswer();
    AnswerType type = mca.getAnswerType();
    
    mcapb.setId(id);
    mcapb.setAnswer(answer);
    if (AnswerType.PICTURE == type){
      mcapb.setType(MultipleChoiceAnswerProto.AnswerType.PICTURE);
    } else if (AnswerType.TEXT == type) {
      mcapb.setType(MultipleChoiceAnswerProto.AnswerType.TEXT);
    }
    
    return mcapb.build();
  }
  
  @Override
  public PictureQuestionProto createPictureQuestionProto(PicturesQuestionWithTextAnswer pqwta) {
    PictureQuestionProto.Builder pqpb = PictureQuestionProto.newBuilder();
    
    String pictureQuestionId = pqwta.getId();
    Set<String> images = pqwta.getImages();
    String answer = pqwta.getAnswer();
    
    pqpb.setId(pictureQuestionId);
    
    //images could be null
    for (String img : images) {
      pqpb.addImageNames(img);
    }
    pqpb.setAnswer(answer);
    
    return pqpb.build();
  }
  
}
