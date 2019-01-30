package me.aboullaite.util;

import org.springframework.security.crypto.bcrypt.BCrypt;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PasswordUtil {

    private static int logRounds = 11;

    /**
     * generate a string representing hashed password
     * @param plain_password
     * @return String hashed_password
     */
    public static String hashPassword(String plain_password){
        String salt = BCrypt.gensalt(logRounds);
        return BCrypt.hashpw(plain_password,salt);
    }

    /**
     *
     * @param plain_password
     * @param hashed
     * @return boolean password matched or not
     */
    public static boolean verifyPassword(String plain_password, String hashed){
        return BCrypt.checkpw(plain_password,hashed);
    }

    public static boolean checkStrength(String pwd){
        int digit = 0;
        int upCount = 0;
        int lowCount = 0;
        int special = 0;

        if(pwd.length() < 8) return false;
        for(int i = 0; i < pwd.length(); i++){
            char c = pwd.charAt(i);
            if(Character.isUpperCase(c)){
                upCount = upCount + 1;
            }
            if(Character.isLowerCase(c)){
                lowCount = lowCount + 1;
            }
            if(Character.isDigit(c)){
                digit = digit + 1;
            }
            if(c>=33&&c<=46||c==64){
                special = special + 1;
            }
        }
        if (special<1 || lowCount<1 || upCount<1 || digit<1) return false;
        return true;
    }

    public static final Pattern VALID_EMAIL_ADDRESS_REGEX =
            Pattern.compile("^([_a-zA-Z0-9-]+(\\.[_a-zA-Z0-9-]+)*@[a-zA-Z0-9-]+(\\.[a-zA-Z0-9-]+)*(\\.[a-zA-Z]{1,6}))$");

    public static boolean validate(String emailStr) {
        Matcher matcher = VALID_EMAIL_ADDRESS_REGEX .matcher(emailStr);
        return matcher.find();
    }



}
