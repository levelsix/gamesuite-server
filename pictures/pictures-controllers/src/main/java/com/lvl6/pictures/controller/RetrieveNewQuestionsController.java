package com.lvl6.pictures.controller;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.lvl6.gamesuite.common.controller.EventController;
import com.lvl6.gamesuite.common.events.RequestEvent;
import com.lvl6.pictures.controller.utils.CreateNoneventProtoUtils;
import com.lvl6.pictures.eventprotos.RetrieveNewQuestionsEventProto.RetrieveNewQuestionsRequestProto;
import com.lvl6.pictures.eventprotos.RetrieveNewQuestionsEventProto.RetrieveNewQuestionsResponseProto;
import com.lvl6.pictures.eventprotos.RetrieveNewQuestionsEventProto.RetrieveNewQuestionsResponseProto.Builder;
import com.lvl6.pictures.eventprotos.RetrieveNewQuestionsEventProto.RetrieveNewQuestionsResponseProto.RetrieveNewQuestionsStatus;
import com.lvl6.pictures.events.request.RetrieveNewQuestionsRequestEvent;
import com.lvl6.pictures.events.response.RetrieveNewQuestionsResponseEvent;
import com.lvl6.pictures.noneventprotos.PicturesEventProtocolProto.PicturesEventProtocolRequest;
import com.lvl6.pictures.noneventprotos.TriviaQuestionFormatProto.QuestionProto;
import com.lvl6.pictures.noneventprotos.UserProto.BasicUserProto;
import com.lvl6.pictures.po.QuestionBase;
import com.lvl6.pictures.services.questionbase.QuestionBaseService;

public class RetrieveNewQuestionsController extends EventController {
  
  private static Logger log = LoggerFactory.getLogger(new Object() { }.getClass().getEnclosingClass());
  
  @Autowired
  protected CreateNoneventProtoUtils createNoneventProtoUtils;
  
  @Autowired
  protected QuestionBaseService questionBaseService;
  
  @Override
  public RequestEvent createRequestEvent() {
    return new RetrieveNewQuestionsRequestEvent();
  }

  @Override
  public int getEventType() {
    return PicturesEventProtocolRequest.C_RETRIEVE_NEW_QUESTIONS_EVENT_VALUE;
  }
  
  @Override
  protected void processRequestEvent(RequestEvent event) throws Exception {
    //stuff client sent
    RetrieveNewQuestionsRequestProto reqProto = 
        ((RetrieveNewQuestionsRequestEvent) event).getRetrieveNewQuestionsRequestProto();
    BasicUserProto sender = reqProto.getSender();
    String userId = sender.getUserId();
    int numQuestionsWanted = reqProto.getNumQuestionsWanted();
    
    //response to send back to client
    Builder responseBuilder = RetrieveNewQuestionsResponseProto.newBuilder();
    responseBuilder.setRecipient(sender);
    responseBuilder.setStatus(RetrieveNewQuestionsStatus.FAIL_OTHER);
    
    RetrieveNewQuestionsResponseEvent resEvent =
        new RetrieveNewQuestionsResponseEvent(userId);
    resEvent.setTag(event.getTag());
    
    try {
      setQuestions(responseBuilder, numQuestionsWanted);
      
      responseBuilder.setStatus(RetrieveNewQuestionsStatus.SUCCESS);

      //write to client
      resEvent.setRetrieveNewQuestionsResponseProto(responseBuilder.build());
      log.info("Writing event: " + resEvent);
      getEventWriter().handleEvent(resEvent);
      
    } catch (Exception e) {
      log.error("exception in RetrieveNewQuestionsController processRequestEvent", e);
      
      try {
        //try to tell client that something failed
        responseBuilder.setStatus(RetrieveNewQuestionsStatus.FAIL_OTHER);
        resEvent.setRetrieveNewQuestionsResponseProto(responseBuilder.build());
        getEventWriter().handleEvent(resEvent);
        
      } catch (Exception e2) {
        log.error("exception in RetrieveNewQuestionsController processRequestEvent", e2);
      }
    }
  }

  private void setQuestions(Builder responseBuilder, int amount) {
    Set<String> allPictureNames = new HashSet<String>();
    List<QuestionProto> newQuestions = new ArrayList<QuestionProto>(); 
    
    List<QuestionBase> questions =
        getQuestionBaseService().getRandomQuestions(amount, allPictureNames);
    
    for(QuestionBase qb : questions) {
      QuestionProto proto =
          getCreateNoneventProtoUtils().createQuestionProto(qb);
      newQuestions.add(proto);
    }
    
    //set responseBuilder
    responseBuilder.addAllNewQuestions(newQuestions);
    responseBuilder.addAllPictureNames(allPictureNames);
  }
  
  public CreateNoneventProtoUtils getCreateNoneventProtoUtils() {
    return createNoneventProtoUtils;
  }

  public void setCreateNoneventProtoUtils(
      CreateNoneventProtoUtils createNoneventProtoUtils) {
    this.createNoneventProtoUtils = createNoneventProtoUtils;
  }

  public QuestionBaseService getQuestionBaseService() {
    return questionBaseService;
  }

  public void setQuestionBaseService(QuestionBaseService questionBaseService) {
    this.questionBaseService = questionBaseService;
  }
  
}