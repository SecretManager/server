package application.file.domain;

import static com.navercorp.fixturemonkey.api.experimental.JavaGetterMethodPropertySelector.javaGetter;
import static org.assertj.core.api.Assertions.assertThat;

import application.member.domain.Member;
import application.member.domain.Membership;
import application.member.domain.MembershipType;
import application.support.MockTestSupport;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;

@DisplayName("암호화된 파일 메타데이터 (FileMetadata) 은(는)")
@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(ReplaceUnderscores.class)
class FileMetadataTest extends MockTestSupport {

    @Test
    void 생성_시_랜덤한_이름으로_생성된다() {
        // given
        Member member = sut.giveMeBuilder(Member.class)
                .set(javaGetter(Member::getId), 1L)
                .sample();

        // when
        FileMetadata metadata = FileMetadata.createRandomName(member.getId(), "origin", 10, "", "");

        // then
        assertThat(metadata.getEncryptedFileName()).isNotEqualTo("origin");
        assertThat(metadata.getEncryptedFileName()).contains("1-");
    }

    @Test
    void 파일_다운로드_시_다운로드_횟수_증가() {
        // given
        Member member = sut.giveMeBuilder(Member.class)
                .set(javaGetter(Member::getId), 1L)
                .set(javaGetter(Member::getMembership), new Membership(MembershipType.FREE))
                .sample();

        FileMetadata metadata = FileMetadata.createRandomName(member.getId(), "origin", 10, "", "");

        // when
        metadata.download(member);

        // then
        assertThat(metadata.getCurrentDownloadCountPerMonth()).isEqualTo(1);
    }

    @Test
    void 파일_다운로드_시_최대_횟수_이상_다운받았다면_예외() {
        // given
        Member member = sut.giveMeBuilder(Member.class)
                .set(javaGetter(Member::getId), 1L)
                .set(javaGetter(Member::getMembership), new Membership(MembershipType.FREE))
                .sample();

        FileMetadata metadata = sut.giveMeBuilder(FileMetadata.class)
                .set(javaGetter(FileMetadata::getCurrentDownloadCountPerMonth), 10)
                .sample();

        // when & then
        Assertions.assertThatThrownBy(() -> {
            metadata.download(member);
        });
    }

    @Test
    void 파일_업로드_시_파일_크기만큼_누적_업로드_크기_증가() {
        // given
        Member member = sut.giveMeBuilder(Member.class)
                .set(javaGetter(Member::getId), 1L)
                .set(javaGetter(Member::getMembership), new Membership(MembershipType.FREE))
                .sample();
        member.uploadFile(10);

        FileMetadata metadata = sut.giveMeBuilder(FileMetadata.class)
                .set(javaGetter(FileMetadata::getFileBytesSize), 100)
                .sample();

        // when
        metadata.upload(member);

        // then
        assertThat(member.getMembership().getCurrentSavedFileBytes()).isEqualTo(110);
    }

    @Test
    void 파일_업로드_시_가능한_파일_크기_이상_업로드_한_경우_예외() {
        // given
        Member member = sut.giveMeBuilder(Member.class)
                .set(javaGetter(Member::getId), 1L)
                .set(javaGetter(Member::getMembership), new Membership(MembershipType.FREE))
                .sample();
        member.uploadFile(100);

        FileMetadata metadata = sut.giveMeBuilder(FileMetadata.class)
                .set(javaGetter(FileMetadata::getFileBytesSize), 4_999_999)
                .sample();

        // when & then
        Assertions.assertThatThrownBy(() -> {
            metadata.upload(member);
        });
    }
}
