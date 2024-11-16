package application.common.algorithm;

import org.mindrot.jbcrypt.BCrypt;

public class Bcrypt {

    public static String encrypt(String text) {
        return BCrypt.hashpw(text, BCrypt.gensalt());
    }

    public static boolean check(String plain, String hashed) {
        return BCrypt.checkpw(plain, hashed);
    }
}
