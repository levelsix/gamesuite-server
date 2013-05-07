package com.lvl6.pictures.controller.utils;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import com.lvl6.gamesuite.common.dao.UserDao;
import com.lvl6.gamesuite.common.po.AuthorizedDevice;
import com.lvl6.gamesuite.common.po.User;
import com.lvl6.pictures.dao.MultipleChoiceQuestionDao;
import com.lvl6.pictures.dao.PictureQuestionWithTextAnswerDao;
import com.lvl6.pictures.noneventprotos.TriviaGameFormatProto.GameResultsProto;
import com.lvl6.pictures.noneventprotos.TriviaGameFormatProto.PlayerGameResultsProto;
import com.lvl6.pictures.noneventprotos.TriviaQuestionFormatProto.MultipleChoiceAnswerProto;
import com.lvl6.pictures.noneventprotos.TriviaQuestionFormatProto.MultipleChoiceQuestionProto;
import com.lvl6.pictures.noneventprotos.TriviaQuestionFormatProto.PictureQuestionProto;
import com.lvl6.pictures.noneventprotos.TriviaQuestionFormatProto.QuestionProto;
import com.lvl6.pictures.noneventprotos.TriviaRoundFormatProto.BasicRoundProto;
import com.lvl6.pictures.noneventprotos.TriviaRoundFormatProto.BasicRoundResultsProto;
import com.lvl6.pictures.noneventprotos.TriviaRoundFormatProto.CompleteRoundResultsProto;
import com.lvl6.pictures.noneventprotos.UserProto.BasicAuthorizedDeviceProto;
import com.lvl6.pictures.noneventprotos.UserProto.BasicUserProto;
import com.lvl6.pictures.po.GameHistory;
import com.lvl6.pictures.po.MultipleChoiceAnswer;
import com.lvl6.pictures.po.MultipleChoiceQuestion;
import com.lvl6.pictures.po.PicturesQuestionWithTextAnswer;
import com.lvl6.pictures.po.QuestionAnswered;
import com.lvl6.pictures.po.RoundHistory;

public interface CreateNoneventProtoUtils {
  
  public abstract BasicUserProto createBasicUserProto(User aUser, AuthorizedDevice ad);
  
  public abstract BasicAuthorizedDeviceProto createBasicAuthorizedDeviceProto(AuthorizedDevice ad,
      String userId);
  
  /**
   * 
   * @param gh - Should not be null.
   * @param userIdToBasicUserProto - Can be null. Only for convenience, since basic user protos can
   * be constructed from gh.
   * @return
   */
  public abstract GameResultsProto createGameResultsProto(GameHistory gh,
      Map<String, BasicUserProto> userIdToBasicUserProto);
  
  public abstract PlayerGameResultsProto createPlayerGameResultsProto(Collection<RoundHistory> rhCollection,
      BasicUserProto bup);
  
  public abstract BasicRoundProto createBasicRoundProto(String roundHistoryId, int roundNumber,
      List<PicturesQuestionWithTextAnswer> pqwta, List<MultipleChoiceQuestion> mcqList);
  
  public abstract BasicRoundResultsProto createBasicRoundResultsProto(RoundHistory rh);
  
  public abstract BasicRoundResultsProto createBasicRoundResultsProto(CompleteRoundResultsProto crrp);
  
  public abstract List<QuestionProto> createQuestionProtoList(List<PicturesQuestionWithTextAnswer> pqwtaList,
      List<MultipleChoiceQuestion> mcqList);
  
  public abstract QuestionProto createQuestionProto(MultipleChoiceQuestion mcq, PicturesQuestionWithTextAnswer pqwta);
  
  public abstract MultipleChoiceQuestionProto createMultipleChoiceQuestionProto(MultipleChoiceQuestion mcq);
  
  public abstract MultipleChoiceAnswerProto createMultipleChoiceAnswerProto(MultipleChoiceAnswer mca);
  
  public abstract PictureQuestionProto createPictureQuestionProto(PicturesQuestionWithTextAnswer pqwta);
  
  public abstract UserDao getUserDao();
  
  public abstract void setUserDao(UserDao userDao);
  
}