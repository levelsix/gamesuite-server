package com.lvl6.gamesuite.common.po;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.annotations.Index;


@Entity
@Table( name = "users" )
public class User extends BasePersistentObject {

	
	@NotNull
	@Size(min = 3, max=30)
	@Column(unique=true)
	@Index(name = "user_name_index")
	protected String name = "";
	
	
	@NotNull
	protected String password;

	
	@NotNull
	@Size(min = 3)
	@Column(unique=true)
	@Index(name = "user_email_index")
	protected String email = "";
	
	
	@Temporal(TemporalType.TIMESTAMP)
	@NotNull
	@Index(name = "user_last_login_index")
	protected Date lastLogin = new Date();

	
	
	@Temporal(TemporalType.TIMESTAMP)
	@NotNull
	@Index(name = "user_signup_date_index")
	protected Date signupDate = new Date();
	
	
	
	protected String facebookId;


	public String getFacebookId() {
		return facebookId;
	}


	public void setFacebookId(String facebookId) {
		this.facebookId = facebookId;
	}


	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}


	public Date getLastLogin() {
		return lastLogin;
	}


	public void setLastLogin(Date lastLogin) {
		this.lastLogin = lastLogin;
	}


	public String getPassword() {
		return password;
	}


	public void setPassword(String password) {
		this.password = password;
	}


	public String getEmail() {
		return email;
	}


	public void setEmail(String email) {
		this.email = email;
	}


	public Date getSignupDate() {
		return signupDate;
	}


	public void setSignupDate(Date signupDate) {
		this.signupDate = signupDate;
	}

	
	
	
}
