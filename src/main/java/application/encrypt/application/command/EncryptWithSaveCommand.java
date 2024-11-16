package application.encrypt.application.command;

import application.encrypt.domain.FileMetadata;
import application.encrypt.domain.key.FolderKey;

public record EncryptWithSaveCommand(
        Long memberId,
        String fileName,
        String description,
        FolderKey folderKey,
        byte[] bytes
) {
    public FileMetadata toFileMetadata() {
        return FileMetadata.createRandomName(memberId, fileName, description);
    }
}
