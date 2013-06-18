package com.lvl6.pictures.po;

import java.util.Set;

import javax.persistence.CollectionTable;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;


@Entity
@Table( name = "pictures_question_with_text_answer" )
public class PicturesQuestionWithTextAnswer extends QuestionBase {
	
  private static final long serialVersionUID = 4088077101766047456L;

  @ElementCollection
	@CollectionTable(name="pictures_question_with_text_answer_images")
	@Size(min=1, max=4)
	protected Set<String> images; 
	
	//space delineated words
	@NotNull
	protected String answer;

	@Override
	public Set<String> getPictureNames() {
	  return images;
	}
	
	
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
