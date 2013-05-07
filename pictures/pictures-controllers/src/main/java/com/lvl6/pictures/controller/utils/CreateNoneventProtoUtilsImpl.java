package com.lvl6.pictures.controller.utils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;

import com.lvl6.gamesuite.common.dao.UserDao;
import com.lvl6.gamesuite.common.po.AuthorizedDevice;
import com.lvl6.gamesuite.common.po.User;
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
import com.lvl6.pictures.noneventprotos.UserProto.BasicAuthorizedDeviceProto.Builder;
import com.lvl6.pictures.noneventprotos.UserProto.BasicUserProto;
import com.lvl6.pictures.po.AnswerType;
import com.lvl6.pictures.po.GameHistory;
import com.lvl6.pictures.po.MultipleChoiceAnswer;
import com.lvl6.pictures.po.MultipleChoiceQuestion;
import com.lvl6.pictures.po.PicturesQuestionWithTextAnswer;
import com.lvl6.pictures.po.RoundHistory;

public class CreateNoneventProtoUtilsImpl implements CreateNoneventProtoUtils {
  
  @Autowired
  protected UserDao userDao; 

  

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
  public GameResultsProto createGameResultsProto(GameHistory gh, Map<String, BasicUserProto> userIdToBasicUserProto) {
    GameResultsProto.Builder grpb = GameResultsProto.newBuilder();
    String gameId = gh.getId();
    
    String playerOneId = gh.getPlayerOneId();
    String playerTwoId = gh.getPlayerTwoId();
    Collection<RoundHistory> playerOneRhs = gh.getRoundHistoryForUser(playerOneId);
    Collection<RoundHistory> playerTwoRhs = gh.getRoundHistoryForUser(playerTwoId);
    BasicUserProto bupPlayerOne = null;
    BasicUserProto bupPlayerTwo = null;
    
    //get player one basic user proto
    if (!userIdToBasicUserProto.containsKey(playerOneId)) {
      User playerOne = getUserDao().findById(playerOneId);
      bupPlayerOne = createBasicUserProto(playerOne, null);
    } else {
      bupPlayerOne = userIdToBasicUserProto.get(playerOneId);
    }
    //get player two basic user proto
    if (!userIdToBasicUserProto.containsKey(playerTwoId)) {
      User playerTwo = getUserDao().findById(playerTwoId);
      bupPlayerTwo = createBasicUserProto(playerTwo, null);
    } else {
      bupPlayerTwo = userIdToBasicUserProto.get(playerTwoId);
    }
    
    PlayerGameResultsProto firstPlayer = createPlayerGameResultsProto(playerOneRhs, bupPlayerOne);
    PlayerGameResultsProto secondPlayer = createPlayerGameResultsProto(playerTwoRhs, bupPlayerTwo);
    
    grpb.setGameId(gameId);
    grpb.setFirstPlayer(firstPlayer);
    grpb.setSecondPlayer(secondPlayer);
    return grpb.build();
  }
  
  //All these RoundHistory (and QuestionAnswered within it) should be for one user
  @Override
  public PlayerGameResultsProto createPlayerGameResultsProto(Collection<RoundHistory> rhCollection,
      BasicUserProto bup) {
    PlayerGameResultsProto.Builder pgrpb = PlayerGameResultsProto.newBuilder();
    
    pgrpb.setBup(bup);
    //add the round result protos
    for(RoundHistory rh : rhCollection) {
      //rh.roundEnded or rh.roundStarted for that matter maybe null, meaning the user has not begun this round
      if (null == rh.getRoundEnded()) {
        continue;
      } else {
        BasicRoundResultsProto brrp = createBasicRoundResultsProto(rh);
        pgrpb.addPreviousRoundsStats(brrp);
      }
    }
    
    return pgrpb.build();
  }
  
  @Override
  public BasicRoundProto createBasicRoundProto(String roundHistoryId, int roundNumber,
      List<PicturesQuestionWithTextAnswer> pqwtaList, List<MultipleChoiceQuestion> mcqList) {
    BasicRoundProto.Builder brpb = BasicRoundProto.newBuilder();
    
    List<QuestionProto> qpList = createQuestionProtoList(pqwtaList, mcqList);
    
    brpb.setId(roundHistoryId);
    brpb.addAllQuestions(qpList);
    brpb.setRoundNumber(roundNumber);
    
    return brpb.build();
  }
  
  @Override
  public BasicRoundResultsProto createBasicRoundResultsProto(RoundHistory rh) {
    BasicRoundResultsProto.Builder brrpb = BasicRoundResultsProto.newBuilder();
    String id = rh.getId();
    int numQuestionsSeen = rh.getQuestionsAnswered().size();
    int numQuestionsAnsweredCorrectly = rh.getCorrectAnswers();
    int score = rh.getScore();
    int roundNumber = rh.getRoundNumber();
    
    brrpb.setId(id);
    brrpb.setNumQuestionsSeen(numQuestionsSeen);
    brrpb.setNumQuestionsAnsweredCorrectly(numQuestionsAnsweredCorrectly);
    brrpb.setScore(score);
    brrpb.setRoundNumber(roundNumber);
    
    return brrpb.build();
  }
  
  public BasicRoundResultsProto createBasicRoundResultsProto(CompleteRoundResultsProto crrp) {
    BasicRoundResultsProto.Builder brpb = BasicRoundResultsProto.newBuilder();
    brpb.setId(crrp.getId());
    brpb.setNumQuestionsSeen(crrp.getNumQuestionsSeen());
    brpb.setNumQuestionsAnsweredCorrectly(crrp.getNumQuestionsAnsweredCorrectly());
    brpb.setScore(crrp.getScore());
    brpb.setRoundNumber(crrp.getRoundNumber());
    return brpb.build();
  }
  
  
  @Override
  public List<QuestionProto> createQuestionProtoList(List<PicturesQuestionWithTextAnswer> pqwtaList,
      List<MultipleChoiceQuestion> mcqList) {
    List<QuestionProto> qpList = new ArrayList<QuestionProto>();
    
    for (PicturesQuestionWithTextAnswer pqwta : pqwtaList) {
      MultipleChoiceQuestion mcq = null;
      QuestionProto qp = createQuestionProto(mcq, pqwta);
      qpList.add(qp);
    }
    
    for (MultipleChoiceQuestion mcq : mcqList) {
      PicturesQuestionWithTextAnswer pqwta = null;
      QuestionProto qp = createQuestionProto(mcq, pqwta);
      qpList.add(qp);
    }
    
    return qpList;
  }
  
  @Override
  public QuestionProto createQuestionProto(MultipleChoiceQuestion mcq,
      PicturesQuestionWithTextAnswer pqwta) {
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
  
  
  @Override
  public UserDao getUserDao() {
    return userDao;
  }

  @Override
  public void setUserDao(UserDao userDao) {
    this.userDao = userDao;
  }
  
}
