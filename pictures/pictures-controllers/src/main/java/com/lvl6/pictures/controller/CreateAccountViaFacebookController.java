package com.lvl6.pictures.controller;

import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.lvl6.gamesuite.common.controller.EventController;
import com.lvl6.gamesuite.common.events.RequestEvent;
import com.lvl6.gamesuite.common.noneventprotos.CommonEventProtocolProto.CommonEventProtocolRequest;
import com.lvl6.gamesuite.common.po.AuthorizedDevice;
import com.lvl6.gamesuite.common.po.User;
import com.lvl6.gamesuite.common.services.authorizeddevice.AuthorizedDeviceService;
import com.lvl6.gamesuite.common.services.user.UserSignupService;
import com.lvl6.pictures.controller.utils.CreateNoneventProtoUtils;
import com.lvl6.pictures.eventprotos.CreateAccountEventProto.CreateAccountResponseProto;
import com.lvl6.pictures.eventprotos.CreateAccountEventProto.CreateAccountResponseProto.Builder;
import com.lvl6.pictures.eventprotos.CreateAccountEventProto.CreateAccountResponseProto.CreateAccountStatus;
import com.lvl6.pictures.eventprotos.CreateAccountEventProto.CreateAccountViaFacebookRequestProto;
import com.lvl6.pictures.events.request.CreateAccountViaFacebookRequestEvent;
import com.lvl6.pictures.events.response.CreateAccountViaFacebookResponseEvent;
import com.lvl6.pictures.noneventprotos.UserProto.BasicUserProto;

@Component  public class CreateAccountViaFacebookController extends EventController {

    private static Logger log = LoggerFactory.getLogger(new Object() { }.getClass().getEnclosingClass());

    @Autowired
    protected UserSignupService userSignupService;

    @Autowired
    protected AuthorizedDeviceService authorizedDeviceService;

    @Autowired
    protected CreateNoneventProtoUtils noneventProtoUtils;


    @Override
    public RequestEvent createRequestEvent() {
	return new CreateAccountViaFacebookRequestEvent();
    }

    @Override
    public int getEventType() {
	return CommonEventProtocolRequest.C_CREATE_ACCOUNT_VIA_FACEBOOK_EVENT_VALUE;
    }

    @Override
    protected void processRequestEvent(RequestEvent event) throws Exception {
	CreateAccountViaFacebookRequestProto reqProto = 
		((CreateAccountViaFacebookRequestEvent) event).getCreateAccountViaFacebookRequestProto();

	//log.info("reqProto=" + reqProto);

	String facebookId = reqProto.getFacebookId();
	String nameFriendsSee = reqProto.getNameFriendsSee();
	String email = null;
	if (reqProto.hasEmail()) {
	    email = reqProto.getEmail();
	}
	String udid = reqProto.getUdid();
	String deviceId = reqProto.getDeviceId();

	//response to send back to client
	CreateAccountResponseProto.Builder responseBuilder = CreateAccountResponseProto.newBuilder();
	responseBuilder.setStatus(CreateAccountStatus.FAIL_OTHER);

	boolean validRequestArgs = isValidRequestArguments(responseBuilder, reqProto,
		facebookId, nameFriendsSee, email, udid);
	boolean validRequest = false;
	boolean success = false;


	if (validRequestArgs) {
	    validRequest = isValidRequest(responseBuilder, facebookId, email, nameFriendsSee, udid, deviceId);
	}

	if(validRequest) {
	    success = writeChangesToDb(responseBuilder, facebookId, nameFriendsSee, email, udid, deviceId);
	}

	if(success) {
	    responseBuilder.setStatus(CreateAccountStatus.SUCCESS_ACCOUNT_CREATED);
	}

	CreateAccountResponseProto resProto = responseBuilder.build();
	//autowire or use new()...
	CreateAccountViaFacebookResponseEvent resEvent =
		new CreateAccountViaFacebookResponseEvent(udid);
	resEvent.setTag(event.getTag());
	resEvent.setCreateAccountResponseProto(resProto);

	log.info("Writing event: " + resEvent);
	getEventWriter().processPreDBResponseEvent(resEvent, udid);
    }

    private boolean isValidRequestArguments(Builder responseBuilder, CreateAccountViaFacebookRequestProto request,
	    String facebookId, String nameFriendsSee, String email, String udid) {

	if (!(request.hasFacebookId()) || facebookId.isEmpty()) {
	    responseBuilder.setStatus(CreateAccountStatus.FAIL_MISSING_FACEBOOK_ID);
	    log.error("unexpected error: no facebookId provided. facebookId:" + facebookId
		    + ", nameFriendsSee:" + nameFriendsSee + ", email:" + email + ", udid:" + udid);    }
	if (!(request.hasNameFriendsSee()) || nameFriendsSee.isEmpty()) {
	    responseBuilder.setStatus(CreateAccountStatus.FAIL_INVALID_NAME);
	    log.error("unexpected error: no nameFriendsSee provided. facebookId:" + facebookId
		    + ", nameFriendsSee:" + nameFriendsSee + ", email:" + email + ", udid:" + udid);
	    return false;
	}

	return true;
    }

    private boolean isValidRequest(Builder responseBuilder, String facebookId, String email,
	    String nameFriendsSee, String udid, String deviceId) {
	String nameStrangersSeeNull = null;
	String udidNull = null;
	List<User> existing = userSignupService.checkForExistingUser(facebookId, nameStrangersSeeNull, email, udidNull);
	if (null != existing && !existing.isEmpty()) {
	    for (User u : existing) {
		if (facebookId.equals(u.getFacebookId())) {
		    responseBuilder.setStatus(CreateAccountStatus.FAIL_DUPLICATE_FACEBOOK_ID);
		    log.error("user error: user already has account with us via facebook. user=" + u);

		} else if (null != email && email.equals(u.getEmail())) {
		    responseBuilder.setStatus(CreateAccountStatus.FAIL_DUPLICATE_EMAIL);
		    log.error("user error: user trying to reuse taken email. user=" + u);

		} else {
		    //maybe just ignore instead and not treat this as a fail...
		    log.error("unexpected error: user returned does not have same facebookId, nor email. user="
			    + u + " args=[facebookId=" + facebookId + ", nameFriendsSee=" + nameFriendsSee + ", email=" + email +
			    ", udid=" + udid + ", deviceId=" + deviceId);
		    responseBuilder.setStatus(CreateAccountStatus.FAIL_OTHER);
		}

	    }
	    return false;
	}

	return true;
    }

    private boolean writeChangesToDb(Builder responseBuilder, String facebookId,
	    String nameFriendsSee, String email, String udid, String deviceId) {
	boolean success = false;

	String nameStrangersSee = userSignupService.generateRandomName(nameFriendsSee);
	//log.info("\t\t\t\t\t nameStrangersSee=" + nameStrangersSee);

	String password = null;
	String userId = null;
	User newUser = null;
	AuthorizedDevice  ad = null;
	try {
	    //create the new user
	    newUser = userSignupService.signup(nameStrangersSee, nameFriendsSee, email, password, facebookId);
	    //need to record the device for the user

	    userId = newUser.getId();
	    Date now = new Date();
	    ad = authorizedDeviceService.registerNewAuthorizedDevice(userId, udid, deviceId, now);

	    BasicUserProto bp = noneventProtoUtils.createBasicUserProto(newUser, ad, null);

	    //log.info("\t\t basicUserProto=" + bp); 
	    responseBuilder.setRecipient(bp);

	    success = true;
	} catch (Exception e) {
	    success = false;
	    log.error("failed to create user or device. user=" + newUser + ", authorizedDevice="+ ad, e);
	}
	return success;
    }


    public UserSignupService getService() {
	return userSignupService;
    }

    public void setService(UserSignupService service) {
	this.userSignupService = service;
    }

    public CreateNoneventProtoUtils getNoneventProtoUtils() {
	return noneventProtoUtils;
    }

    public void setNoneventProtoUtils(CreateNoneventProtoUtils noneventProtoUtils) {
	this.noneventProtoUtils = noneventProtoUtils;
    }

}