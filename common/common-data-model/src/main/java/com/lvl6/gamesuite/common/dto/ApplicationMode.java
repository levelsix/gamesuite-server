package com.lvl6.gamesuite.common.dto;

import java.io.Serializable;

public class ApplicationMode implements Serializable {


	private static final long serialVersionUID = 5507441630474140699L;
	protected boolean isMaintenanceMode = false;
	protected String messageForUsers = "";

	
	public boolean isMaintenanceMode() {
		return isMaintenanceMode;
	}
	public void setMaintenanceMode(boolean isMaintenanceMode) {
		this.isMaintenanceMode = isMaintenanceMode;
	}
	public String getMessageForUsers() {
		return messageForUsers;
	}
	public void setMessageForUsers(String messageForUsers) {
		this.messageForUsers = messageForUsers;
	}
}
