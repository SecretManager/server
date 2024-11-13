package application.encryptor;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import lombok.Getter;

@Getter
public class Nonce {

    private static final int NONCE_BYTE_LENGTH = 12;
    private final String nonce;

    public Nonce(byte[] nonce) {
        validateLength(nonce);
        this.nonce = new String(nonce);
    }

    private void validateLength(byte[] nonce) {
        if (nonce.length != NONCE_BYTE_LENGTH) {
            throw new IllegalArgumentException("Nonce 는 %d 바이트여야 합니다. 입력된 바이트 : %d"
                    .formatted(NONCE_BYTE_LENGTH, nonce.length));
        }
    }

    public static Nonce generate() {
        byte[] iv = new byte[NONCE_BYTE_LENGTH];
        try {
            SecureRandom.getInstanceStrong().nextBytes(iv);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
        return new Nonce(iv);
    }

    public static Nonce extractFromPrefix(InputStream inputStream) {
        try {
            byte[] iv = new byte[NONCE_BYTE_LENGTH];
            inputStream.read(iv);
            return new Nonce(iv);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public byte[] getBytes() {
        byte[] bytes = new byte[NONCE_BYTE_LENGTH];
        System.arraycopy(nonce.getBytes(StandardCharsets.UTF_8), 0, bytes, 0, NONCE_BYTE_LENGTH);
        return bytes;
    }
}
