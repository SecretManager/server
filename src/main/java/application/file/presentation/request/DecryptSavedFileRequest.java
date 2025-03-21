package application.file.presentation.request;

import application.file.application.command.DecryptSavedFileCommand;
import application.member.domain.Member;
import jakarta.annotation.Nullable;

public record DecryptSavedFileRequest(
        @Nullable String folderKey
) {
    public DecryptSavedFileCommand toCommand(Long fileId, Member member) {
        return new DecryptSavedFileCommand(
                fileId,
                member,
                folderKey
        );
    }
}
