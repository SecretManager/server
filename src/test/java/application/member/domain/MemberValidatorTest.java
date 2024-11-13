package application.member.domain;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.BDDMockito.given;

import application.support.MockTestSupport;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

@DisplayName("회원 검증기 (MemberValidator) 은(는)")
@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(ReplaceUnderscores.class)
class MemberValidatorTest extends MockTestSupport {

    @Mock
    private MemberRepository memberRepository;

    @InjectMocks
    private MemberValidator validator;

    @Test
    void 회원가입_시_아이디가_중복되면_예외() {
        // given
        String username = "user";
        given(memberRepository.existsByUsername(username)).willReturn(true);

        // when & then
        Assertions.assertThatThrownBy(() -> {
            validator.validateSignup(username);
        });
    }

    @Test
    void 회원가입_시_아이디가_중복되지_않으면_통과() {
        // given
        String username = "user";
        given(memberRepository.existsByUsername(username)).willReturn(false);

        // when & then
        assertDoesNotThrow(() -> {
            validator.validateSignup(username);
        });
    }
}
