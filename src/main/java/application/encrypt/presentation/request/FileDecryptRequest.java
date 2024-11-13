package application.encrypt.presentation.request;

import application.encrypt.application.command.FileDecryptCommand;
import application.member.domain.Member;
import jakarta.annotation.Nullable;

public record FileDecryptRequest(
        @Nullable Long id,
        String plainKey
) {
    public FileDecryptCommand toCommand(Member member) {
        return new FileDecryptCommand(id, member.getId(), plainKey);
    }
}
