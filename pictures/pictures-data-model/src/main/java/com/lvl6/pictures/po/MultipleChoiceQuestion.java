package com.lvl6.pictures.po;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;


@Entity
@Table( name = "multiple_choice_question" )
public class MultipleChoiceQuestion extends QuestionBase {
	
	private static final long serialVersionUID = 45782438787935651L;

  	@NotNull
	protected String question = "";
	
	@OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)	
	@Size(min=2, max=6)
	protected Set<MultipleChoiceAnswer> answers = new HashSet<MultipleChoiceAnswer>();

	public String getQuestion() {
		return question;
	}

	//when client gets questions to answer, client wants
	//collection of picture names
	@Override
	public Set<String> getPictureNames() {
	  Set<String> picNames = new HashSet<String>();
	  
	  for (MultipleChoiceAnswer mca : answers) {
	    String picName = mca.getPictureName();
	    if (null != picName) {
	      picNames.add(picName);
	    }
	  }
	  return picNames;
	}
	
	
	public void setQuestion(String question) {
		this.question = question;
	}

	public Set<MultipleChoiceAnswer> getAnswers() {
		return answers;
	}

	public void setAnswers(Set<MultipleChoiceAnswer> answers) {
		this.answers = answers;
	}

	@Override
	public String toString() {
		return "MultipleChoiceQuestion [question=" + question + ", answers=" + answers + ", createdDate="
				+ createdDate + ", createdBy=" + createdBy + ", id=" + id + ", version=" + version + "]";
	}

	
}
