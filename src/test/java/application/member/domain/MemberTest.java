package application.member.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willThrow;
import static org.mockito.Mockito.mock;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@DisplayName("회원 (Member) 은(는)")
@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(ReplaceUnderscores.class)
class MemberTest {

    private final MemberValidator memberValidator = mock(MemberValidator.class);
    private final PasswordEncryptor encryptor = mock(PasswordEncryptor.class);

    @Nested
    class 회원가입_시 {

        @Test
        void 회원가입_조건_검증에_실패하면_예외() {
            // given
            Member member = new Member("user", "pss", "email@email.com");
            willThrow(new RuntimeException())
                    .given(memberValidator)
                    .validateSignup(eq("user"));

            // when & then
            assertThatThrownBy(() -> {
                member.signup(memberValidator, encryptor);
            });
        }

        @Test
        void 회원가입_조건_검증에_통과하면_비밀번호가_암호화된다() {
            // given
            Member member = new Member("user", "pss", "email@email.com");
            given(encryptor.encrypt(eq("pss")))
                    .willReturn("encrypted");

            // when
            member.signup(memberValidator, encryptor);

            // then
            assertThat(member.getPlainPassword()).isNull();
            assertThat(member.getHashedPassword()).isEqualTo("encrypted");
        }
    }

    @Nested
    class 로그인_시 {

        @Test
        void 비밀번호가_일치하지_않으면_실패() {
            // given
            Member member = new Member(1L, "user", "hashed", "email@email.com");
            given(encryptor.checkPassword(eq("plain"), eq("hashed")))
                    .willReturn(false);

            // when & then
            assertThatThrownBy(() -> {
                member.login(encryptor, "plain");
            });
        }

        @Test
        void 비밀번호가_일치하면_성공() {
            // given
            Member member = new Member(1L, "user", "hashed", "email@email.com");
            given(encryptor.checkPassword(eq("plain"), eq("hashed")))
                    .willReturn(true);

            // when & then
            Assertions.assertDoesNotThrow(() -> {
                member.login(encryptor, "plain");
            });
        }
    }
}
