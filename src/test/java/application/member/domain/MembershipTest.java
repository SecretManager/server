package application.member.domain;

import static com.navercorp.fixturemonkey.api.experimental.JavaGetterMethodPropertySelector.javaGetter;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import application.support.MockTestSupport;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

@DisplayName("멤버십 (Membership) 은(는)")
@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(ReplaceUnderscores.class)
class MembershipTest extends MockTestSupport {

    @Nested
    class 파일_업로드_시 {

        @Test
        void 누적_업로드_파일_크기_증가() {
            // given
            Member member = sut.giveMeBuilder(Member.class)
                    .set(javaGetter(Member::getMembership), new Membership(MembershipType.ADVANCED))
                    .sample();
            member.uploadFile(299_990);

            // when
            member.uploadFile(10);

            // then
            assertThat(member.getMembership().getCurrentSavedFileBytes()).isEqualTo(300_000);
        }

        @Test
        void 최대_업로드_한계에_다다르면_예외() {
            // given
            Member member = sut.giveMeBuilder(Member.class)
                    .set(javaGetter(Member::getMembership), new Membership(MembershipType.FREE))
                    .sample();
            member.uploadFile(299_998);

            // when & then
            assertThatThrownBy(() -> {
                member.uploadFile(10);
            });
        }
    }

    @Nested
    class 파일_다운로드_시 {

        @Test
        void 이번달_누적_다운로드_횟수가_최대_다운로드_횟수보다_작다면_가능() {
            // given
            Member member = sut.giveMeBuilder(Member.class)
                    .set(javaGetter(Member::getMembership), new Membership(MembershipType.FREE))
                    .sample();

            // when & then
            assertDoesNotThrow(() -> {
                member.downloadFile(9);
            });
        }

        @ParameterizedTest
        @ValueSource(ints = {10, 11})
        void 이번달_누적_다운로드_횟수가_최대_다운로드_횟수보다_크거나_같다면_예외(int value) {
            // given
            Member member = sut.giveMeBuilder(Member.class)
                    .set(javaGetter(Member::getMembership), new Membership(MembershipType.FREE))
                    .sample();

            // when & then
            assertThatThrownBy(() -> {
                member.downloadFile(value);
            });
        }
    }
}
