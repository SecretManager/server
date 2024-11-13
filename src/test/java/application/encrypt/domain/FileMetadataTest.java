package application.encrypt.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;

@DisplayName("암호화된 파일 메타데이터 (FileMetadata) 은(는)")
@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(ReplaceUnderscores.class)
class FileMetadataTest {

    @Test
    void 생성_시_랜덤한_이름으로_생성된다() {
        // when
        FileMetadata metadata = FileMetadata.createRandomName(1L, "origin");

        // then
        assertThat(metadata.getEncryptedFileName()).isNotEqualTo("origin");
        assertThat(metadata.getEncryptedFileName()).contains("1-");
    }
}
