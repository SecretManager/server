package application.member.application.command;

public record LoginCommand(
        String username,
        String plainPassword
) {
}
