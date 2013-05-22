package com.lvl6.pictures.controller.utils;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import com.lvl6.gamesuite.common.dao.UserDao;
import com.lvl6.gamesuite.common.po.AuthorizedDevice;
import com.lvl6.gamesuite.common.po.User;
import com.lvl6.pictures.noneventprotos.TriviaGameFormatProto.GameResultsProto;
import com.lvl6.pictures.noneventprotos.TriviaGameFormatProto.OngoingGameProto;
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
import com.lvl6.pictures.po.QuestionBase;
import com.lvl6.pictures.po.RoundHistory;
import com.lvl6.pictures.po.RoundPendingCompletion;

public interface CreateNoneventProtoUtils {
  
  public abstract Map<String, BasicUserProto> createIdsToBasicUserProtos(List<GameHistory> ghList);
  
  public abstract Map<String, BasicUserProto> createIdsToBasicUserProtos(Collection<String> userIds);
  
  public abstract BasicUserProto createBasicUserProto(User aUser, AuthorizedDevice ad);
  
  public abstract BasicAuthorizedDeviceProto createBasicAuthorizedDeviceProto(AuthorizedDevice ad,
      String userId);
  
  
  public abstract List<OngoingGameProto> createOngoingGameProtosForUser(List<GameHistory> ghList,
      Map<String, BasicUserProto> idsToBasicUserProtos, String userId, boolean isUserTurn);
  
  /**
   * The OngoingGameProto returned contains either a GameResultsProto paired with a BasicRoundProto
   * or just a GameResultsProto. 
   * @param gh
   * @param idsToBasicUserProtos - for convenience/efficiency
   * @param userId - The OngoingGameProto will be constructed from the point of view of this user.
   * @param isUserTurn - whether or not user userId goes next for this OngoingGameProto
   * @return
   */
  public abstract OngoingGameProto createOngoingGameProtoForUser(GameHistory gh,
      Map<String, BasicUserProto> idsToBasicUserProtos, String userId, boolean isUserTurn);
  
  public abstract List<GameResultsProto> createGameResultsProtos(List<GameHistory> ghList,
      Map<String, BasicUserProto> idsToBasicUserProtos);
  
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
  
  public abstract BasicRoundProto createBasicRoundProto(RoundPendingCompletion rpc);
  
  public abstract BasicRoundProto createBasicRoundProto(RoundHistory rh);
  
  public abstract BasicRoundResultsProto createBasicRoundResultsProto(RoundHistory rh);
  
  public abstract BasicRoundResultsProto createBasicRoundResultsProto(CompleteRoundResultsProto crrp);
  
  public abstract QuestionProto createQuestionProto(QuestionAnswered qa);
  
  public abstract QuestionProto createQuestionProto(QuestionBase qb);
  
  public abstract MultipleChoiceQuestionProto createMultipleChoiceQuestionProto(MultipleChoiceQuestion mcq);
  
  public abstract MultipleChoiceAnswerProto createMultipleChoiceAnswerProto(MultipleChoiceAnswer mca);
  
  public abstract PictureQuestionProto createPictureQuestionProto(PicturesQuestionWithTextAnswer pqwta);
  
  public abstract UserDao getUserDao();
  
  public abstract void setUserDao(UserDao userDao);
  
}