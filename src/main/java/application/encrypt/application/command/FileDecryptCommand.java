package application.encrypt.application.command;

public record FileDecryptCommand(
        Long id,
        Long memberId,
        String plainKey
) {
}
