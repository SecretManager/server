package application.encrypt.domain;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import application.encrypt.domain.key.FolderKey;
import application.encrypt.domain.key.KeyChain;
import application.encrypt.domain.key.PersonalKey;
import application.encrypt.domain.key.SecretKeyGenerator;
import application.encrypt.domain.key.ServerKey;
import application.encrypt.domain.key.ServerKey.ServerSecretKeyProperty;
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
    private final Encryptor encryptor = new Encryptor(secretKeyGenerator);

    @Nested
    class 암호화_시 {

        @Test
        void 여러_키로_암호화_가능() {
            // given
            KeyChain keyChain = new KeyChain(
                    FolderKey.ofPlainKeyForEncrypt("1", null),
                    new PersonalKey(null, null, "2"),
                    new ServerKey(new ServerSecretKeyProperty("3"))
            );
            String target = "target";

            // when & then
            assertDoesNotThrow(() -> {
                encryptor.encrypt(target.getBytes(UTF_8), keyChain);
            });
        }
    }

    @Nested
    class 복호화_시 {

        @Test
        void 여러_키로_복호화_가능() {
            // given
            KeyChain keyChain = new KeyChain(
                    FolderKey.ofPlainKeyForEncrypt("1", null),
                    new PersonalKey(null, null, "2"),
                    new ServerKey(new ServerSecretKeyProperty("3"))
            );
            String target = "target";
            byte[] encrypt = encryptor.encrypt(target.getBytes(UTF_8), keyChain);

            // when
            byte[] decrypt = encryptor.decrypt(encrypt, keyChain);

            // then
            assertThat(new String(decrypt, UTF_8)).isEqualTo(target);
        }
    }
}
