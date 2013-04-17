package com.lvl6.gamesuite.amqp.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.lvl6.gamesuite.common.events.GameEvent;
import com.lvl6.gamesuite.common.events.ResponseEvent;

public abstract class EventWriter  {

	
	private static Logger log = LoggerFactory.getLogger(EventWriter.class);
	public EventWriter() {
		super();
	}

	public void handleEvent(GameEvent event) {
		try {
			processEvent(event);
		} catch (Exception e) {
			log.error("Error handling event: {}", event, e);
		}
	}
	

	protected abstract void processEvent(GameEvent event) throws Exception;

	
	public abstract void processGlobalChatResponseEvent(ResponseEvent event);

	public abstract void processPreDBResponseEvent(ResponseEvent event, String udid);
	
	public void handleClanEvent(ResponseEvent event, int clanId) {
		try {
			processClanResponseEvent(event, clanId);
		} catch (Exception e) {
			log.error("Error handling clan event: " + event, e);
		}
	}

	public abstract void processClanResponseEvent(ResponseEvent event, int clanId);
	
	//public abstract void sendAdminMessage(String message);
	
}