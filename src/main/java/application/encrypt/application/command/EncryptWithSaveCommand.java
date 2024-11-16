package application.encrypt.application.command;

import application.encrypt.domain.key.FolderKey;

public record EncryptWithSaveCommand(
        Long memberId,
        String fileName,
        FolderKey folderKey,
        byte[] bytes
) {
}
