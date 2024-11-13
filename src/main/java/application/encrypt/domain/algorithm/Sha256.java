package application.encrypt.domain.algorithm;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Sha256 {

    private static final String HEX_FORMAT = "%02x";

    public static String encrypt(String text) {
        try {
            // MessageDigest 는 thread-safe 하지 않음
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.update(text.getBytes());
            return bytesToHex(md.digest());
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    private static String bytesToHex(byte[] bytes) {
        StringBuilder builder = new StringBuilder();
        for (byte b : bytes) {
            builder.append(String.format(HEX_FORMAT, b));
        }
        return builder.toString();
    }

    public static boolean check(String text, String expectedHash) {
        String encryptedText = encrypt(text);
        return encryptedText.equals(expectedHash);  // 해시값이 일치하면 true 반환
    }
}
