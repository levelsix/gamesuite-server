package com.lvl6.gamesuite.common.po;

import java.util.Date;
import java.util.UUID;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.hibernate.annotations.Index;



@Entity
@Table( name = "authorized_devices" )
public class AuthorizedDevice extends BasePersistentObject {

	/**
     * 
     */
    private static final long serialVersionUID = 5881408831551873312L;


	@NotNull
	@Index(name = "authorized_device_token_expires_index")
	protected String userId;
	
	
	@NotNull
	@Index(name = "authorized_device_udid_index")
	protected String udid;
	
	
	@NotNull
	@Index(name = "authorized_device_token_index")
	protected String token = UUID.randomUUID().toString();
	
	
	@Temporal(TemporalType.TIMESTAMP)
	@NotNull
	@Index(name = "authorized_device_token_expires_index")
	protected Date expires;
	

	@Temporal(TemporalType.TIMESTAMP)
	@NotNull
	@Index(name = "authorized_device_token_created_index")
	protected Date created = new Date();
	
	
//	@NotNull
	protected String deviceId; //for Apple Push Notifications



  public String getUserId() {
		return userId;
	}


	public void setUserId(String userId) {
		this.userId = userId;
	}


	public String getUdid() {
		return udid;
	}


	public void setUdid(String udid) {
		this.udid = udid;
	}


	public String getToken() {
		return token;
	}


	public void setToken(String token) {
		this.token = token;
	}


	public Date getExpires() {
		return expires;
	}


	public void setExpires(Date expires) {
		this.expires = expires;
	}


	public Date getCreated() {
		return created;
	}


	public void setCreated(Date created) {
		this.created = created;
	}

//  no need to personally track the device type
//	public String getDeviceType() {
//		return deviceType;
//	}
//
//
//	public void setDeviceType(String deviceType) {
//		this.deviceType = deviceType;
//	}
	
	public String getDeviceId() {
	  return deviceId;
	}
	
	
	public void setDeviceId(String deviceId) {
	  this.deviceId = deviceId;
	}
	
	@Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }
	
}
