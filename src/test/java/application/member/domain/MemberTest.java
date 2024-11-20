package application.member.domain;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
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

    @Nested
    class 회원가입_시 {

        @Test
        void 회원가입_조건_검증에_실패하면_예외() {
            // given
            Member member = Member.preSignup("user", "pss", "email@email.com");
            willThrow(new RuntimeException())
                    .given(memberValidator)
                    .validateSignup(member);

            // when & then
            assertThatThrownBy(() -> {
                member.signup(memberValidator);
            });
        }
    }

    @Nested
    class 로그인_시 {

        @Test
        void 비밀번호가_일치하지_않으면_실패() {
            // given
            Member member = Member.preSignup("user", "pwd", "email@email.com");

            // when & then
            assertThatThrownBy(() -> {
                member.login("pwd1");
            });
        }

        @Test
        void 비밀번호가_일치하면_성공() {
            // given
            Member member = Member.preSignup("user", "pwd", "email@email.com");

            // when & then
            Assertions.assertDoesNotThrow(() -> {
                member.login("pwd");
            });
        }
    }
}
