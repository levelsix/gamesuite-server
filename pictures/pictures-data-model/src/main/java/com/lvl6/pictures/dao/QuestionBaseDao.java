package com.lvl6.pictures.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.lvl6.pictures.po.QuestionBase;


public interface QuestionBaseDao extends JpaRepository<QuestionBase, String>  {

}
