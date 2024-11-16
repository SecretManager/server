package application.encrypt.application.command;

import application.encrypt.domain.key.FolderKey;

public record DecryptSavedFileCommand(
        Long id,
        Long memberId,
        FolderKey folderKey
) {
}
