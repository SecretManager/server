package application.member.presentation.request;

import jakarta.validation.constraints.NotBlank;

public record SignupRequest(
        @NotBlank String name,
        @NotBlank String username,
        @NotBlank String plainPassword,
        @NotBlank String email
) {
}
