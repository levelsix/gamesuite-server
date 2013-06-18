package com.lvl6.pictures.controller;

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
import com.lvl6.gamesuite.user.utils.EmailUtil;
import com.lvl6.pictures.controller.utils.CreateNoneventProtoUtils;
import com.lvl6.pictures.eventprotos.CreateAccountEventProto.CreateAccountResponseProto;
import com.lvl6.pictures.eventprotos.CreateAccountEventProto.CreateAccountResponseProto.Builder;
import com.lvl6.pictures.eventprotos.CreateAccountEventProto.CreateAccountResponseProto.CreateAccountStatus;
import com.lvl6.pictures.eventprotos.CreateAccountEventProto.CreateAccountViaEmailRequestProto;
import com.lvl6.pictures.events.request.CreateAccountViaEmailRequestEvent;
import com.lvl6.pictures.events.response.CreateAccountViaEmailResponseEvent;
import com.lvl6.pictures.noneventprotos.UserProto.BasicUserProto;

@Component  public class CreateAccountViaEmailController extends EventController {

    private static Logger log = LoggerFactory.getLogger(new Object() { }.getClass().getEnclosingClass());

    @Autowired
    protected UserSignupService userSignupService;

    @Autowired
    protected AuthorizedDeviceService authorizedDeviceService;


    @Autowired
    protected CreateNoneventProtoUtils noneventProtoUtils;


    @Override
    public RequestEvent createRequestEvent() {
	return new CreateAccountViaEmailRequestEvent();
    }

    @Override
    public int getEventType() {
	return CommonEventProtocolRequest.C_CREATE_ACCOUNT_VIA_EMAIL_EVENT_VALUE;
    }

    @Override
    protected void processRequestEvent(RequestEvent event) throws Exception {
	CreateAccountViaEmailRequestProto reqProto = 
		((CreateAccountViaEmailRequestEvent) event).getCreateAccountViaEmailRequestProto();

	String nameStrangersSee = reqProto.getNameStrangersSee();
	String email = reqProto.getEmail();
	String password = reqProto.getPassword();
	String udid = reqProto.getUdid();
	String deviceId = reqProto.getDeviceId();

	//response to send back to client
	CreateAccountResponseProto.Builder responseBuilder = CreateAccountResponseProto.newBuilder();
	responseBuilder.setStatus(CreateAccountStatus.FAIL_OTHER);
	CreateAccountViaEmailResponseEvent resEvent =  new CreateAccountViaEmailResponseEvent(udid);
	resEvent.setTag(event.getTag());

	try {
	    boolean validRequestArgs = isValidRequestArguments(responseBuilder, reqProto,
		    nameStrangersSee, email, password, udid);
	    boolean validRequest = false;
	    boolean success = false;


	    if (validRequestArgs) {
		validRequest = isValidRequest(responseBuilder, nameStrangersSee, email, udid, deviceId);
	    }

	    if(validRequest) {
		success = writeChangesToDb(responseBuilder, nameStrangersSee, email, password, udid, deviceId);
	    }

	    if(success) {
		responseBuilder.setStatus(CreateAccountStatus.SUCCESS_ACCOUNT_CREATED);
	    }

	    CreateAccountResponseProto resProto = responseBuilder.build();
	    //autowire or use new()...
	    resEvent.setCreateAccountResponseProto(resProto);

	    log.info("Writing event: " + resEvent);
	    getEventWriter().processPreDBResponseEvent(resEvent, udid);
	} catch (Exception e) {
	    log.error("exception2 in CreateAccountViaEmailController processRequestEvent", e);

	    try {
		//try to tell client that something failed
		responseBuilder.setStatus(CreateAccountStatus.FAIL_OTHER);
		resEvent.setCreateAccountResponseProto(responseBuilder.build());
		getEventWriter().processPreDBResponseEvent(resEvent, udid);
	    } catch (Exception e2) {
		log.error("exception in CreateAccountViaEmailController processRequestEvent", e2);
	    }
	}
    }

    private boolean isValidRequestArguments(Builder responseBuilder, CreateAccountViaEmailRequestProto request,
	    String nameStrangersSee, String email, String password, String udid) {
	if (!(request.hasNameStrangersSee()) || nameStrangersSee.isEmpty()) {
	    responseBuilder.setStatus(CreateAccountStatus.FAIL_INVALID_NAME);
	    log.error("unexpected error: no name provided. password:" + password
		    + ", nameStrangersSee:" + nameStrangersSee + ", email:" + email + ", udid:" + udid);
	    return false;
	}
	if (!(request.hasEmail()) || email.isEmpty() || !EmailUtil.isValidEmailAddressFormat(email)) {
	    responseBuilder.setStatus(CreateAccountStatus.FAIL_INVALID_EMAIL);
	    log.error("user error: email given is invalid. email=" + email);
	    return false;
	}
	if (!(request.hasPassword()) || password.isEmpty()) {
	    responseBuilder.setStatus(CreateAccountStatus.FAIL_INVALID_PASSWORD);
	    log.error("unexpected error: no password provided. password:" + password
		    + ", nameStrangersSee:" + nameStrangersSee + ", email:" + email + ", udid:" + udid);    
	    return false;
	}

	return true;
    }

    private boolean isValidRequest(Builder responseBuilder, String nameStrangersSee, String email,
	    String udid, String deviceId) {
	String facebookIdNull = null;
	String udidNull = null;
	List<User> existing = userSignupService.checkForExistingUser(facebookIdNull, nameStrangersSee, email, udidNull);

	if (null != existing && !existing.isEmpty()) {
	    for (User u: existing) {
		if (nameStrangersSee.equalsIgnoreCase(u.getNameStrangersSee())) { //ignore case for now
		    responseBuilder.setStatus(CreateAccountStatus.FAIL_DUPLICATE_NAME);
		    log.error("user error: Either name in use by another or user already has " +
			    "account with us. user=" + existing);
		} else if (email.equals(u.getEmail())) {
		    responseBuilder.setStatus(CreateAccountStatus.FAIL_DUPLICATE_EMAIL);
		    log.error("user error: Either email in use by another or user already has " +
			    "account with us. user=" + existing);
		} else {
		    //maybe just ignore instead and not treat this as a fail...
		    log.error("unexpected error: user returned does not have same name, nor email. user=" + u +
			    " args=[nameStrangersSee=" + nameStrangersSee + ", email=" + email +
			    ", udid=" + udid + ", deviceId=" + deviceId);
		    responseBuilder.setStatus(CreateAccountStatus.FAIL_OTHER);
		}

	    }
	    return false;
	}

	return true;
    }

    private boolean writeChangesToDb(Builder responseBuilder, String nameStrangersSee,
	    String email, String password, String udid, String deviceId) {
	boolean success = false;

	String nameFriendsSeeNull = null;
	String facebookId = null;
	String userId = null;
	User newUser = null;
	AuthorizedDevice  ad = null;
	try {
	    //create the new user
	    newUser = userSignupService.signup(nameStrangersSee, nameFriendsSeeNull, email, password, facebookId);
	    //need to record the device for the user

	    userId = newUser.getId();
	    ad = authorizedDeviceService.registerNewAuthorizedDevice(userId, udid, deviceId);

	    BasicUserProto bp =
		    noneventProtoUtils.createBasicUserProto(newUser, ad, password);
	    responseBuilder.setRecipient(bp);

	    success = true;
	} catch (Exception e) {
	    success = false;
	    log.error("unexpected error: failed to create user or device. user=" +
		    newUser + ", authorizedDevice="+ ad, e);
	}
	return success;
    }

    public UserSignupService getService() {
	return userSignupService;
    }

    public void setService(UserSignupService service) {
	this.userSignupService = service;
    }

    public AuthorizedDeviceService getAuthorizedDeviceService() {
	return authorizedDeviceService;
    }

    public void setAuthorizedDeviceService(
	    AuthorizedDeviceService authorizedDeviceService) {
	this.authorizedDeviceService = authorizedDeviceService;
    }

    public CreateNoneventProtoUtils getNoneventProtoUtils() {
	return noneventProtoUtils;
    }

    public void setNoneventProtoUtils(CreateNoneventProtoUtils noneventProtoUtils) {
	this.noneventProtoUtils = noneventProtoUtils;
    }

}