package com.lvl6.pictures.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.lvl6.pictures.po.QuestionAnswered;

public interface QuestionAnsweredDao extends JpaRepository<QuestionAnswered, String> {

  //TODO: GET THIS TO WORK (GETTING UNIQUE QUESTION IDS)
  List<String> findDistinctQuestionIdByAnsweredByUser(String userId);
  
}
