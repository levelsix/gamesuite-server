package com.lvl6.pictures.events.request;

import java.nio.ByteBuffer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.protobuf.ByteString;
import com.google.protobuf.InvalidProtocolBufferException;
import com.lvl6.pictures.eventprotos.CreateAccountEventProto.CreateAccountViaFacebookRequestProto;
import com.lvl6.gamesuite.common.events.PreDatabaseRequestEvent;

public class CreateAccountViaFacebookRequestEvent extends PreDatabaseRequestEvent {

    private static Logger log = LoggerFactory.getLogger(new Object() { }.getClass().getEnclosingClass());

    private CreateAccountViaFacebookRequestProto createAccountViaFacebookRequestProto;

    /**
     * read the event from the given ByteBuffer to populate this event
     */
    public void read(ByteBuffer buff) {
	try {
	    createAccountViaFacebookRequestProto = CreateAccountViaFacebookRequestProto.parseFrom(ByteString.copyFrom(buff));

	    //Player id is "" since it won't be initialized yet.
	    playerId = "";

	    udid = createAccountViaFacebookRequestProto.getUdid();
	} catch (InvalidProtocolBufferException e) {
	    log.error("unexpected error: CreateAccountViaFacebookRequestEvent. ", e);
	}
    }

    public CreateAccountViaFacebookRequestProto getCreateAccountViaFacebookRequestProto() {
	return createAccountViaFacebookRequestProto;
    }

    public void setCreateAccountViaFacebookRequestProto(CreateAccountViaFacebookRequestProto createAccountViaFacebookRequestProto) {
	this.createAccountViaFacebookRequestProto = createAccountViaFacebookRequestProto;
    }
}
