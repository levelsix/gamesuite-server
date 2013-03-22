package com.lvl6.pictures.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.lvl6.pictures.po.MultipleChoiceQuestion;

public interface MultipleChoiceQuestionDao extends JpaRepository<MultipleChoiceQuestion, String> {

}
