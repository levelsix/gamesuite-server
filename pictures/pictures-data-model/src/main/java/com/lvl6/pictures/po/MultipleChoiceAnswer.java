package com.lvl6.pictures.po;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.Index;

import com.lvl6.gamesuite.common.po.BasePersistentObject;


@Entity
@Table( name = "multiple_choice_answer" )
public class MultipleChoiceAnswer extends BasePersistentObject implements Serializable {
  
  private static final long serialVersionUID = -671451609994013459L;

  protected boolean isCorrect;
	
	@NotNull
	protected String answer;

	
	@NotNull
	@Enumerated(EnumType.STRING)
	@Index(name="multiple_choice_answer_type_index")
	protected AnswerType answerType;
	
	//if answer is a picture return the name, purpose is: 
	//when client gets questions to answer, client wants
	//collection of picture names
	public String getPictureName() {
	  if(AnswerType.PICTURE == answerType) {
	    return answer;
	  } else {
	    return null;
	  }
	  
	}
	
	
	
	public boolean isCorrect() {
		return isCorrect;
	}

	public void setCorrect(boolean isCorrect) {
		this.isCorrect = isCorrect;
	}

	public String getAnswer() {
		return answer;
	}

	public void setAnswer(String answer) {
		this.answer = answer;
	}

	public AnswerType getAnswerType() {
	  return answerType;
	}

	public void setAnswerType(AnswerType answerType) {
	  this.answerType = answerType;
	}
	


	
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((answer == null) ? 0 : answer.hashCode());
		return result;
	}



	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		MultipleChoiceAnswer other = (MultipleChoiceAnswer) obj;
		if (answer == null) {
			if (other.answer != null)
				return false;
		} else if (!answer.equals(other.answer))
			return false;
		return true;
	}



	@Override
	public String toString() {
		return "MultipleChoiceAnswer [isCorrect=" + isCorrect + ", answer=" + answer + ", answerType="
				+ answerType + ", id=" + id + ", version=" + version + "]";
	}


	
	

}
