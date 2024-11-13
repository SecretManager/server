package application.member.application.command;

import application.member.domain.Member;
import java.util.Optional;

public record SignupCommand(
        String username,
        String plainPassword,
        Optional<String> email
) {
    public Member toMember() {
        return new Member(username, plainPassword, email.orElse(null));
    }
}
