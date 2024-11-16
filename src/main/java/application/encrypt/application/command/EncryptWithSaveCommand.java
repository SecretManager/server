package application.encrypt.application.command;

import application.encrypt.domain.FileMetadata;

public record EncryptWithSaveCommand(
        Long memberId,
        String fileName,
        String description,
        String folderKey,
        String hint,
        byte[] bytes
) {
    public FileMetadata toFileMetadata() {
        return FileMetadata.createRandomName(memberId, fileName, description, hint);
    }
}
