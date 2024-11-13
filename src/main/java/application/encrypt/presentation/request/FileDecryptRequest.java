package application.encrypt.presentation.request;

import application.encrypt.application.command.FileDecryptCommand;
import application.member.domain.Member;
import jakarta.annotation.Nullable;

public record FileDecryptRequest(
        @Nullable String plainKey
) {
    public FileDecryptCommand toCommand(Long fileId, Member member) {
        return new FileDecryptCommand(fileId, member.getId(), plainKey);
    }
}
