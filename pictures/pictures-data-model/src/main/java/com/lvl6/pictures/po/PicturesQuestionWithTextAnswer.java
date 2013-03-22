package com.lvl6.pictures.po;

import java.util.Set;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;


@Entity
@Table( name = "pictures_question_with_text_answer" )
public class PicturesQuestionWithTextAnswer extends QuestionBase {
	
	
	@ElementCollection
	@Size(min=1, max=4)
	protected Set<String> images; 
	
	
	@NotNull
	protected String answer;


	public Set<String> getImages() {
		return images;
	}


	public void setImages(Set<String> images) {
		this.images = images;
	}


	public String getAnswer() {
		return answer;
	}


	public void setAnswer(String answer) {
		this.answer = answer;
	}
}
