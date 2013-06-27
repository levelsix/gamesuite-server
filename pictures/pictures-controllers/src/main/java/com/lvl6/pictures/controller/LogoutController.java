package com.lvl6.pictures.controller;

import java.util.Map;

import javax.annotation.Resource;

import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.lvl6.gamesuite.common.controller.EventController;
import com.lvl6.gamesuite.common.dto.ConnectedPlayer;
import com.lvl6.gamesuite.common.events.RequestEvent;
import com.lvl6.gamesuite.common.noneventprotos.CommonEventProtocolProto.CommonEventProtocolRequest;
import com.lvl6.gamesuite.common.po.User;
import com.lvl6.gamesuite.common.services.user.LoginService;
import com.lvl6.pictures.eventprotos.LogoutEventProto.LogoutRequestProto;
import com.lvl6.pictures.events.request.LogoutRequestEvent;
import com.lvl6.pictures.noneventprotos.UserProto.BasicUserProto;

@Component
public class LogoutController extends EventController {

    private static Logger log = LoggerFactory.getLogger(new Object() { }.getClass().getEnclosingClass());

    public Map<Integer, ConnectedPlayer> getPlayersByPlayerId() {
	return playersByPlayerId;
    }

    public void setPlayersByPlayerId(Map<Integer, ConnectedPlayer> playersByPlayerId) {
	this.playersByPlayerId = playersByPlayerId;
    }

    @Resource(name="playersByPlayerId")
    protected Map<Integer, ConnectedPlayer> playersByPlayerId;

    @Autowired
    LoginService loginService;
    

    @Override
    public RequestEvent createRequestEvent() {
	return new LogoutRequestEvent();
    }

    @Override
    public int getEventType() {
	return CommonEventProtocolRequest.C_LOGOUT_EVENT_VALUE;
    }

    @Override
    protected void processRequestEvent(RequestEvent event) throws Exception {
	//stuff client sent
	LogoutRequestProto reqProto = 
		((LogoutRequestEvent) event).getLogoutRequestProto();
	BasicUserProto sender = reqProto.getSender();
	String userId = sender.getUserId();
	
	try {
	    //get the user
	    User u = getLoginService().getUserById(userId);
	    if (null == u) {
		log.error("unexpected error: no user with id exists. id=" +
			userId + "\t BasicUserProto=" + sender);
	    } else {
		//record the user's logout time
		getLoginService().updateUserLastLogout(u, new DateTime());
		
		//remove from collection/cache of online players 
		playersByPlayerId.remove(userId);
		
		log.info("Player logged out: " + userId);
	    }

	} catch (Exception e) {
	    log.error("exception in LogoutController processRequestEvent", e);
	}
    }
    
    
    public LoginService getLoginService() {
	return loginService;
    }
    
    public void setLoginService(LoginService loginService) {
	this.loginService = loginService;
    }

}