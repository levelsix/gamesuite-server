package com.lvl6.gamesuite.common.po;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;


@Entity
@Table( name = "users" )
public class User extends BasePersistentObject {

	
	@NotNull
	@Size(min = 3)
	@Column(unique=true)
	protected String name = "";
	
	@NotNull
	@Size(min = 10)
	protected String udid = "";
	
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "last_login")
	@NotNull
	protected Date lastLogin;


	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}


	public String getUdid() {
		return udid;
	}


	public void setUdid(String udid) {
		this.udid = udid;
	}


	public Date getLastLogin() {
		return lastLogin;
	}


	public void setLastLogin(Date lastLogin) {
		this.lastLogin = lastLogin;
	}

	
	
	
}
