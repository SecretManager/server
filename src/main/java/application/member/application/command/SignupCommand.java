package application.member.application.command;

import application.member.domain.Member;

public record SignupCommand(
        String username,
        String plainPassword,
        String email,
        String name
) {
    public Member toMember() {
        return Member.preSignup(name, username, plainPassword, email);
    }
}
