package com.lvl6.pictures.po;


import java.util.Date;

import javax.persistence.MappedSuperclass;
import javax.validation.constraints.NotNull;

import com.lvl6.gamesuite.common.po.BasePersistentObject;

@MappedSuperclass
public class QuestionBase extends BasePersistentObject {
	
	
	@NotNull
	protected Date createdDate = new Date();
	
	protected String createdBy;

	public Date getCreatedDate() {
		return createdDate;
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
	
	
	
}
