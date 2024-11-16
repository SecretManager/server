package application.encrypt.domain;

import application.common.Default;
import java.util.UUID;
import lombok.Getter;

@Getter
public class FileMetadata {

    private Long id;
    private Long memberId;
    private String fileName;
    private String description;

    // (암호화 된 파일의) 이름. (이름이 암호화된 것이 아닌, '암호화 된 파일'의 이름임)
    private String encryptedFileName;
    private String keyHint;

    public FileMetadata(
            Long memberId,
            String fileName,
            String description,
            String encryptedFileName,
            String keyHint
    ) {
        this(null, memberId, fileName, description, encryptedFileName, keyHint);
    }

    @Default
    public FileMetadata(
            Long id,
            Long memberId,
            String fileName,
            String description,
            String encryptedFileName,
            String keyHint
    ) {
        this.id = id;
        this.memberId = memberId;
        this.fileName = fileName;
        this.description = description;
        this.encryptedFileName = encryptedFileName;
        this.keyHint = keyHint;
    }

    public static FileMetadata createRandomName(
            Long memberId,
            String fileName,
            String description,
            String keyHint
    ) {
        return new FileMetadata(
                memberId,
                fileName,
                description,
                memberId + "-" + UUID.randomUUID(),
                keyHint
        );
    }
}
