package application.encrypt.domain;

import application.common.Default;
import java.util.UUID;
import lombok.Getter;

@Getter
public class FileMetadata {

    private Long id;
    private Long memberId;
    private String originalFileName;

    // (암호화 된 파일의) 이름. (이름이 암호화된 것이 아닌, '암호화 된 파일'의 이름임)
    private String encryptedFileName;

    public FileMetadata(String originalFileName) {
        this(null, null, originalFileName, null);
    }

    public FileMetadata(Long memberId, String originalFileName, String encryptedFileName) {
        this(null, memberId, originalFileName, encryptedFileName);
    }

    @Default
    public FileMetadata(Long id, Long memberId, String originalFileName, String encryptedFileName) {
        this.id = id;
        this.memberId = memberId;
        this.originalFileName = originalFileName;
        this.encryptedFileName = encryptedFileName;
    }

    public static FileMetadata createRandomName(
            Long memberId,
            String originalFileName
    ) {
        return new FileMetadata(
                memberId,
                originalFileName,
                memberId + "-" + UUID.randomUUID()
        );
    }
}
