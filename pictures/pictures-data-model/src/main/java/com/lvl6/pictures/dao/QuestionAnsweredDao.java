package com.lvl6.pictures.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.lvl6.pictures.po.QuestionAnswered;

public interface QuestionAnsweredDao extends JpaRepository<QuestionAnswered, String> {

}
