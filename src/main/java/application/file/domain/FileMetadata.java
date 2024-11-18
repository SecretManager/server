package application.file.domain;

import application.common.Default;
import application.member.domain.Member;
import java.util.UUID;
import lombok.Getter;

@Getter
public class FileMetadata {

    private Long id;
    private Long memberId;
    private String fileName;
    private long fileBytesSize;
    private String description;

    // (암호화 된 파일의) 이름. (이름이 암호화된 것이 아닌, '암호화 된 파일'의 이름임)
    private String encryptedFileName;
    private String keyHint;
    private int currentDownloadCountPerMonth;

    public FileMetadata(
            Long memberId,
            String fileName,
            long fileBytesSize,
            String description,
            String encryptedFileName,
            String keyHint,
            int currentDownloadCountPerMonth
    ) {
        this(null, memberId, fileName, fileBytesSize, description, encryptedFileName, keyHint,
                currentDownloadCountPerMonth);
    }

    @Default
    public FileMetadata(
            Long id,
            Long memberId,
            String fileName,
            long fileBytesSize,
            String description,
            String encryptedFileName,
            String keyHint,
            int currentDownloadCountPerMonth
    ) {
        this.id = id;
        this.memberId = memberId;
        this.fileName = fileName;
        this.fileBytesSize = fileBytesSize;
        this.description = description;
        this.encryptedFileName = encryptedFileName;
        this.keyHint = keyHint;
        this.currentDownloadCountPerMonth = currentDownloadCountPerMonth;
    }

    public static FileMetadata createRandomName(
            Long memberId,
            String fileName,
            long fileSize,
            String description,
            String keyHint
    ) {
        return new FileMetadata(
                memberId,
                fileName,
                fileSize,
                description,
                memberId + "-" + UUID.randomUUID(),
                keyHint,
                0
        );
    }

    public void upload(Member member) {
        member.uploadFile(fileBytesSize);
    }

    public void download(Member member) {
        member.downloadFile(currentDownloadCountPerMonth);
        currentDownloadCountPerMonth++;
    }
}
