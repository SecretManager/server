package application.encrypt.application.command;

import application.encrypt.domain.key.FolderKey;

public record EncryptWithoutSaveCommand(
        FolderKey folderKey,
        byte[] bytes
) {
}
