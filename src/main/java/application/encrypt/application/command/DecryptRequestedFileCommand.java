package application.encrypt.application.command;

import application.encrypt.domain.key.FolderKey;

public record DecryptRequestedFileCommand(
        byte[] encrypted, FolderKey folderKey
) {
}
