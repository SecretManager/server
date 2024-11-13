package application.member.presentation.request;

public record LoginRequest(
        String username,
        String plainPassword
) {
}
