package application.member.application;


import static com.navercorp.fixturemonkey.api.experimental.JavaGetterMethodPropertySelector.javaGetter;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import application.member.application.command.LoginCommand;
import application.member.application.command.SignupCommand;
import application.member.domain.Member;
import application.member.persistence.MemberEntity;
import application.support.IntegrationTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@DisplayName("회원 서비스 (MemberService) 은(는)")
@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(ReplaceUnderscores.class)
class MemberServiceTest extends IntegrationTest {

    @Nested
    class 회원가입_시 {

        @Test
        void 아이디_중복_시_실패() {
            // given
            String duplicatedUsername = "username";
            MemberEntity member = sut.giveMeBuilder(MemberEntity.class)
                    .set(javaGetter(Member::getId), null)
                    .set(javaGetter(Member::getUsername), duplicatedUsername)
                    .sample();
            memberEntityRepository.save(member);
            SignupCommand command = sut.giveMeBuilder(SignupCommand.class)
                    .set(javaGetter(SignupCommand::username), duplicatedUsername)
                    .sample();

            // when & then
            assertThatThrownBy(() -> {
                memberService.signup(command);
            });
        }

        @Test
        void 성공() {
            // given
            SignupCommand command = sut.giveMeOne(SignupCommand.class);

            // when & then
            assertDoesNotThrow(() -> {
                memberService.signup(command);
            });
        }
    }

    @Nested
    class 로그인_시 {

        String username = "username";
        String password = "password";

        @BeforeEach
        void setUp() {
            SignupCommand command = sut.giveMeBuilder(SignupCommand.class)
                    .set(javaGetter(SignupCommand::username), username)
                    .set(javaGetter(SignupCommand::plainPassword), password)
                    .sample();
            memberService.signup(command);
        }

        @Test
        void 아이디_오류로_실패() {
            // given
            LoginCommand loginCommand = new LoginCommand(username + "diff", password);

            // when & then
            assertThatThrownBy(() -> {
                memberService.login(loginCommand);
            });
        }

        @Test
        void 비밀번호_오류로_실패() {
            // given
            LoginCommand loginCommand = new LoginCommand(username, password + "diff");

            // when & then
            assertThatThrownBy(() -> {
                memberService.login(loginCommand);
            });
        }

        @Test
        void 성공() {
            // given
            LoginCommand loginCommand = new LoginCommand(username, password);

            // when & then
            assertDoesNotThrow(() -> {
                memberService.login(loginCommand);
            });
        }
    }
}
