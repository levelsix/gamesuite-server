package com.lvl6.gamesuite.common.po;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.hibernate.annotations.Index;

@Entity
@Table(name = "users")
public class User extends BasePersistentObject {

	@NotNull
	@Size(min = 3, max = 30)
	@Column(unique = true)
	@Index(name = "user_name_strangers_see_index")
	protected String nameStrangersSee = "";

	//@Size(min = 3, max = 30)
	@Index(name = "user_name_friends_see_index")
	// not sure if this index is needed
	protected String nameFriendsSee = "";

	protected String password;

	// @Size(min = 3)
	@Column(unique = true)
	@Index(name = "user_email_index")
	protected String email = null;

	@Temporal(TemporalType.TIMESTAMP)
	@NotNull
	@Index(name = "user_last_login_index")
	protected Date lastLogin = new Date();

	@Temporal(TemporalType.TIMESTAMP)
	@NotNull
	@Index(name = "user_signup_date_index")
	protected Date signupDate = new Date();

	protected String facebookId;

	public String getNameStrangersSee() {
		return nameStrangersSee;
	}

	public void setNameStrangersSee(String nameStrangersSee) {
		this.nameStrangersSee = nameStrangersSee;
	}

	public String getNameFriendsSee() {
		return nameFriendsSee;
	}

	public void setNameFriendsSee(String nameFriendsSee) {
		this.nameFriendsSee = nameFriendsSee;
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

	public String getFacebookId() {
		return facebookId;
	}

	public void setFacebookId(String facebookId) {
		this.facebookId = facebookId;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}

}
