package me.aboullaite.util;

import org.springframework.security.crypto.bcrypt.BCrypt;

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


}
