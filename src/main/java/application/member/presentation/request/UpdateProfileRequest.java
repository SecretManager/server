package application.member.presentation.request;

import application.member.application.command.UpdateProfileCommand;
import application.member.domain.Member;
import jakarta.validation.constraints.NotBlank;

public record UpdateProfileRequest(
        @NotBlank String name,
        @NotBlank String email
) {
    public UpdateProfileCommand toCommand(Member member) {
        return new UpdateProfileCommand(member, name, email);
    }
}
