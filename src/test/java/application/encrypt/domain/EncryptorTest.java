package application.encrypt.domain;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import javax.crypto.SecretKey;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@DisplayName("암호화기 (Encryptor) 은(는)")
@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(ReplaceUnderscores.class)
class EncryptorTest {

    private final SecretKeyGenerator secretKeyGenerator = new SecretKeyGenerator();
    private final Encryptor encryptor = new Encryptor();

    @Nested
    class 암호화_시 {

        @Test
        void 여러_키로_암호화_가능() {
            // given
            SecretKey key1 = secretKeyGenerator.generateAESKey("key1");
            SecretKey key2 = secretKeyGenerator.generateAESKey("key2");
            String target = "target";

            // when & then
            assertDoesNotThrow(() -> {
                encryptor.encrypt(target.getBytes(UTF_8), key1, key2);
            });
        }
    }

    @Nested
    class 복호화_시 {

        @Test
        void 여러_키로_복호화_가능() {
            // given
            SecretKey key1 = secretKeyGenerator.generateAESKey("key1");
            SecretKey key2 = secretKeyGenerator.generateAESKey("key2");
            String target = "target";
            byte[] encrypt = encryptor.encrypt(target.getBytes(UTF_8), key1, key2);

            // when
            byte[] decrypt = encryptor.decrypt(encrypt, key2, key1);

            // then
            assertThat(new String(decrypt, UTF_8)).isEqualTo(target);
        }

        @Test
        void 중간에_키가_없으면_생략하고_복호화() {
            // given
            SecretKey key1 = secretKeyGenerator.generateAESKey("key1");
            SecretKey key2 = secretKeyGenerator.generateAESKey("key2");
            String target = "target";
            byte[] encrypt = encryptor.encrypt(target.getBytes(UTF_8),
                    null, key1, null, key2, null
            );

            // when
            byte[] decrypt = encryptor.decrypt(encrypt, key2, key1);

            // then
            assertThat(new String(decrypt, UTF_8)).isEqualTo(target);
        }
    }
}
