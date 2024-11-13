package application.encrypt.domain;

import jakarta.annotation.Nullable;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class SecretKeyGenerator {

    public @Nullable SecretKey generateAESKey(@Nullable String plainKey) {
        if (plainKey == null) {
            return null;
        }
        try {
            MessageDigest sha256Digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = sha256Digest.digest(plainKey.getBytes(StandardCharsets.UTF_8));
            return new SecretKeySpec(hash, 0, 32, "AES");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
