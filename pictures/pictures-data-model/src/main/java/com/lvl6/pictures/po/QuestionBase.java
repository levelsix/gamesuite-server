package com.lvl6.pictures.po;


import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.Index;

import com.lvl6.gamesuite.common.po.BasePersistentObject;

//@MappedSuperclass
@Entity
abstract public class QuestionBase extends BasePersistentObject implements Serializable{
	
  private static final long serialVersionUID = -4681717482297820858L;

  	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "created_date")
	@NotNull
	@Index(name="question_created_date_index")
	protected Date createdDate = new Date();
	
	protected String createdBy;

	public Date getCreatedDate() {
		return createdDate;
	}

	//when client gets questions to answer, client wants
	//collection of picture names
	public Set<String> getPictureNames() {
	  return new HashSet<String>();
	}
	
	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	@Override
	public String toString() {
		return "QuestionBase [createdDate=" + createdDate + ", createdBy=" + createdBy + ", id=" + id
				+ ", version=" + version + "]";
	}

	


	
	
}
