package com.lvl6.pictures.webapp.test;

import junit.framework.TestCase;

import org.apache.wicket.util.tester.WicketTester;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.lvl6.pictures.ui.admin.QuestionEditorPage;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("/test-spring-application-context.xml")
public class TestQuestionEditor extends TestCase {
	
	
	protected WicketTester test;
	private static final Logger log = LoggerFactory.getLogger(TestQuestionEditor.class);
	
	
	@Test
	public void testQuestionEditor() {
		test = new WicketTester();
		test.startPage(QuestionEditorPage.class);
	}
}
