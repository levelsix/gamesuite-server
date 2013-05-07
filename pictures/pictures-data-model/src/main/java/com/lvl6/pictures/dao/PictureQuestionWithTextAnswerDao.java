package com.lvl6.pictures.dao;

import java.util.Collection;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.lvl6.pictures.po.PicturesQuestionWithTextAnswer;


public interface PictureQuestionWithTextAnswerDao extends JpaRepository<PicturesQuestionWithTextAnswer, String>  {

  public List<PicturesQuestionWithTextAnswer> findByIdIn(Collection<String> ids);
  
}
