package com.cNerds.dailyMoment.core.util;

import java.util.Base64;
import java.util.Random;

import org.springframework.security.crypto.bcrypt.BCrypt;

public class CryptoUtil {
    
    public static String getHashedPassword(String str){
        return BCrypt.hashpw(str, BCrypt.gensalt(12));
    }
    
    public static Boolean isEqual(String str, String hashed){
        return BCrypt.checkpw(str, hashed);
    }
    
    public static String encode(String str){
    	
    	return Base64.getUrlEncoder().encodeToString(str.getBytes());
    }
    
    public static String decode(String str){
    	return new String(Base64.getUrlDecoder().decode(str));
    }
    
    public static String emailAuthCode() {
    	int leftLimit = 48; // numeral '0'
    	int rightLimit = 122; // letter 'z'
    	int targetStringLength = 10;
    	Random random = new Random();

    	String emailAuthCode = random.ints(leftLimit,rightLimit + 1)
    	  .filter(i -> (i <= 57 || i >= 65) && (i <= 90 || i >= 97))
    	  .limit(targetStringLength)
    	  .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
    	  .toString();

    	return emailAuthCode;
    	
    }
    
}
