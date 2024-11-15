package application.member.presentation.request;

import java.util.Optional;

public record SignupRequest(
        String username,
        String plainPassword,
        Optional<String> email
) {
}
