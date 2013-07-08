package com.lvl6.pictures.events.request;

import java.nio.ByteBuffer;

import com.google.protobuf.ByteString;
import com.google.protobuf.InvalidProtocolBufferException;
import com.lvl6.gamesuite.common.events.RequestEvent;
import com.lvl6.pictures.eventprotos.StartRoundEventProto.StartRoundRequestProto;

public class StartRoundRequestEvent extends RequestEvent {

    private StartRoundRequestProto startRoundRequestProto;

    @Override
    public void read(ByteBuffer bb) {
	try {
	    startRoundRequestProto = StartRoundRequestProto.parseFrom(ByteString.copyFrom(bb));
	    playerId = startRoundRequestProto.getSender().getUserId();
	} catch (InvalidProtocolBufferException e) {
	    e.printStackTrace();
	}
    }

    public StartRoundRequestProto getStartRoundRequestProto() {
	return startRoundRequestProto;
    }

    public void setStartRoundRequestProto(
	    StartRoundRequestProto startRoundRequestProto) {
	this.startRoundRequestProto = startRoundRequestProto;
    }

}
