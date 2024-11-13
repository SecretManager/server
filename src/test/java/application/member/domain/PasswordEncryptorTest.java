package application.member.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;

@DisplayName("비밀번호 암호화기 (PasswordEncryptor) 은(는)")
@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(ReplaceUnderscores.class)
class PasswordEncryptorTest {

    private final PasswordEncryptor passwordEncryptor = new PasswordEncryptor();

    @Test
    void 비밀번호_암호화() {
        // when
        String plain = passwordEncryptor.encrypt("plain");

        // then
        assertThat(plain).isNotEqualTo("plain");
    }

    @Test
    void 비밀번호_체크() {
        // given
        String hashed = passwordEncryptor.encrypt("plain");

        // when
        boolean same = passwordEncryptor.checkPassword("plain", hashed);
        boolean different = passwordEncryptor.checkPassword("different", hashed);

        // then
        assertThat(same).isTrue();
        assertThat(different).isFalse();
    }
}
