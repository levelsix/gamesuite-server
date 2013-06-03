package com.lvl6.pictures.webapp.test;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;




@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("/spring-application-context.xml")
public class TestPicturesApp {

	
	private static final Logger log = LoggerFactory.getLogger(TestPicturesApp.class);
	
	@Test
	public void testSpringContext() {
		log.info("Loaded Spring application context");
	}
}
