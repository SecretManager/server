package application.encrypt.application.command;

import application.encrypt.domain.FileMetadata;
import application.member.domain.Member;

public record EncryptWithSaveCommand(
        Member member,
        String fileName,
        long fileSize,
        String description,
        String folderKey,
        String hint,
        byte[] bytes
) {
    public FileMetadata toFileMetadata() {
        return FileMetadata.createRandomName(member.getId(), fileName, fileSize, description, hint);
    }
}
