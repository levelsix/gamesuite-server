package com.lvl6.pictures.dao;

import java.util.Collection;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.lvl6.pictures.po.MultipleChoiceQuestion;

public interface MultipleChoiceQuestionDao extends JpaRepository<MultipleChoiceQuestion, String> {
  
  public List<MultipleChoiceQuestion> findByIdIn(Collection<String> ids);
  
}
