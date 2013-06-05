package com.lvl6.gamesuite.user.utils;

import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class EmailUtil{
	
	protected static Logger log = LoggerFactory.getLogger(EmailUtil.class);
	
	
	public static boolean isValidEmailAddressFormat(String email) {
    boolean result = true;
    try {
       InternetAddress emailAddr = new InternetAddress(email);
       emailAddr.validate();
    } catch (AddressException ex) {
       result = false;
    }
    return result;
 }
	
}


