package application.member.application.command;

import application.member.domain.Member;

public record SignupCommand(
        String username,
        String plainPassword,
        String email
) {
    public Member toMember() {
        return Member.preSignup(username, plainPassword, email);
    }
}
