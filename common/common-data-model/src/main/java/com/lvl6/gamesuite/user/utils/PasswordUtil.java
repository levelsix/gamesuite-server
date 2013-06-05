package com.lvl6.gamesuite.user.utils;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import net.iharder.Base64;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Component;

@Component
public class PasswordUtil{
	
	protected static Logger log = LoggerFactory.getLogger(PasswordUtil.class);
	
	
	public static String encryptPassword(String password, byte[] salt){
		MessageDigest d;
		try {
			d = MessageDigest.getInstance("SHA-512");
			d.reset();
			d.update(salt);
			byte[] digested = d.digest(password.getBytes("UTF-8"));
			for (int i = 0; i < 3; i++) {
		           d.reset();
		           d.update(salt);
		           digested = d.digest(digested);
		    }
			String hashedPassword = new String(Base64.encodeBytes(digested));
			return hashedPassword;
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException(e.getMessage());
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException(e.getMessage());
		}
	}

	public String encodePassword(String rawPass, byte[] salt){
		return PasswordUtil.encryptPassword(rawPass, salt);
	}

	public boolean isPasswordValid(String encPass, String rawPass, byte[] salt)
			throws DataAccessException {
		String tmp = encodePassword(rawPass, salt);
		if(encPass.equals(tmp)){
			log.info("Passwords match");
			return true;
		}
		log.info("Passwords do not match");
		return false;
	}
	
}


