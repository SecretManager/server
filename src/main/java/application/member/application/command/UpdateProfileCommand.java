package application.member.application.command;

import application.member.domain.Member;

public record UpdateProfileCommand(
        Member member,
        String name,
        String email
) {
}
