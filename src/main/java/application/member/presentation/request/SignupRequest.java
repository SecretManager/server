package application.member.presentation.request;

public record SignupRequest(
        String username,
        String plainPassword,
        String email
) {
}
