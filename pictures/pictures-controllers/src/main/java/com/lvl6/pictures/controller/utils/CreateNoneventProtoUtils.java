package com.lvl6.pictures.controller.utils;

import com.lvl6.gamesuite.common.po.AuthorizedDevice;
import com.lvl6.gamesuite.common.po.User;
import com.lvl6.pictures.noneventprotos.TriviaQuestionFormatProto.MultipleChoiceAnswerProto;
import com.lvl6.pictures.noneventprotos.TriviaQuestionFormatProto.MultipleChoiceQuestionProto;
import com.lvl6.pictures.noneventprotos.TriviaQuestionFormatProto.PictureQuestionProto;
import com.lvl6.pictures.noneventprotos.TriviaQuestionFormatProto.QuestionProto;
import com.lvl6.pictures.noneventprotos.UserProto.BasicAuthorizedDeviceProto;
import com.lvl6.pictures.noneventprotos.UserProto.BasicUserProto;
import com.lvl6.pictures.po.MultipleChoiceAnswer;
import com.lvl6.pictures.po.MultipleChoiceQuestion;
import com.lvl6.pictures.po.PicturesQuestionWithTextAnswer;

public interface CreateNoneventProtoUtils {
  
  public abstract BasicUserProto createBasicUserProto (User aUser, AuthorizedDevice ad);
  
  public abstract BasicAuthorizedDeviceProto createBasicAuthorizedDeviceProto(AuthorizedDevice ad,
      String userId);
  
  public abstract QuestionProto createQuestionProto(MultipleChoiceQuestion mcq, PicturesQuestionWithTextAnswer pqwta);
  
  public abstract MultipleChoiceQuestionProto createMultipleChoiceQuestionProto(MultipleChoiceQuestion mcq);
  
  public abstract MultipleChoiceAnswerProto createMultipleChoiceAnswerProto(MultipleChoiceAnswer mca);
  
  public abstract PictureQuestionProto createPictureQuestionProto(PicturesQuestionWithTextAnswer pqwta);
}