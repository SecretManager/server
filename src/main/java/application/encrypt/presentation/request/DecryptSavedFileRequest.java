package application.encrypt.presentation.request;

import application.encrypt.application.command.DecryptSavedFileCommand;
import application.encrypt.domain.key.FolderKey;
import application.member.domain.Member;
import jakarta.annotation.Nullable;

public record DecryptSavedFileRequest(
        @Nullable String folderKey
) {
    public DecryptSavedFileCommand toCommand(Long fileId, Member member) {
        return new DecryptSavedFileCommand(fileId, member.getId(), FolderKey.fromPlainKeyForDecrypt(folderKey));
    }
}
