package com.lvl6.pictures.persistence.test;

import java.util.HashSet;
import java.util.Set;

import javax.annotation.Resource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.lvl6.pictures.dao.MultipleChoiceQuestionDao;
import com.lvl6.pictures.dao.PictureQuestionWithTextAnswerDao;
import com.lvl6.pictures.po.PicturesQuestionWithTextAnswer;




@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("/spring-db-pictures-context.xml")
public class TestPicturesJpa {
	
	Logger log = LoggerFactory.getLogger(getClass());
	
	@Resource
	protected PictureQuestionWithTextAnswerDao pictureDao;
	
	@Resource 
	protected MultipleChoiceQuestionDao multipleChoiceDao;
	
	@Test
	public void testSetup() {
		log.info("Testing Pictures JPA");
		PicturesQuestionWithTextAnswer q = new PicturesQuestionWithTextAnswer();
		q.setAnswer("The Answer");
		Set<String> images = new HashSet<String>();
		images.add("image1");
		images.add("image2");
		images.add("image3");
		images.add("image4");
		q.setImages(images);
		q.setCreatedBy("JUnit");
		q = getPictureDao().save(q);
		log.info("Created Picture question: {}", q.getId());
	}



	public PictureQuestionWithTextAnswerDao getPictureDao() {
		return pictureDao;
	}



	public void setPictureDao(PictureQuestionWithTextAnswerDao pictureDao) {
		this.pictureDao = pictureDao;
	}



	public MultipleChoiceQuestionDao getMultipleChoiceDao() {
		return multipleChoiceDao;
	}



	public void setMultipleChoiceDao(MultipleChoiceQuestionDao multipleChoiceDao) {
		this.multipleChoiceDao = multipleChoiceDao;
	}






}
