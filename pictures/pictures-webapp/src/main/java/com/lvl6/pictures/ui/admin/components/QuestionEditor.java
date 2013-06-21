package com.lvl6.pictures.ui.admin.components;

import com.lvl6.pictures.po.QuestionBase;

public interface QuestionEditor<T extends QuestionBase> {
	
	public T getQuestion();
	public void setQuestion(T question);
}
