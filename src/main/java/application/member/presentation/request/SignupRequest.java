package application.member.presentation.request;

import application.member.domain.Member;
import java.util.Optional;

public record SignupRequest(
        String username,
        String plainPassword,
        Optional<String> email
) {
    public Member toMember() {
        return new Member(username, plainPassword, email.orElse(null));
    }
}
