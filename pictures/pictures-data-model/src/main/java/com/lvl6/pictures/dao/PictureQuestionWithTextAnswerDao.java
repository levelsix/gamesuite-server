package com.lvl6.pictures.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.lvl6.pictures.po.PicturesQuestionWithTextAnswer;


public interface PictureQuestionWithTextAnswerDao extends JpaRepository<PicturesQuestionWithTextAnswer, String>  {

}
