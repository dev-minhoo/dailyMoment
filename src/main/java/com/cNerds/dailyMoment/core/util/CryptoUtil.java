package com.cNerds.dailyMoment.core.util;

import org.springframework.security.crypto.bcrypt.BCrypt;

public class CryptoUtil {
    
    public static String getHashedPassword(String str){
        return BCrypt.hashpw(str, BCrypt.gensalt(12));
    }
    
    public static Boolean isEqual(String str, String hashed){
        return BCrypt.checkpw(str, hashed);
    }
    
}
