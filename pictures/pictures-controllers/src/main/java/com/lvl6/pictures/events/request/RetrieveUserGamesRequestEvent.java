package com.lvl6.pictures.events.request;

import java.nio.ByteBuffer;

import com.google.protobuf.ByteString;
import com.google.protobuf.InvalidProtocolBufferException;
import com.lvl6.gamesuite.common.events.RequestEvent;
import com.lvl6.pictures.eventprotos.RetrieveUserGamesEventProto.RetrieveUserGamesRequestProto;

public class RetrieveUserGamesRequestEvent extends RequestEvent {

    private RetrieveUserGamesRequestProto retrieveUserGamesRequestProto;

    @Override
    public void read(ByteBuffer bb) {
	try {
	    retrieveUserGamesRequestProto = RetrieveUserGamesRequestProto.parseFrom(ByteString.copyFrom(bb));
	    playerId = retrieveUserGamesRequestProto.getSender().getUserId();
	} catch (InvalidProtocolBufferException e) {
	    e.printStackTrace();
	}
    }

    public RetrieveUserGamesRequestProto getRetrieveUserGamesRequestProto() {
	return retrieveUserGamesRequestProto;
    }

}
