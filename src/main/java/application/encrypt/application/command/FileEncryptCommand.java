package application.encrypt.application.command;

public record FileEncryptCommand(
        Long memberId,
        String fileName,
        String plainKey,
        byte[] bytes
) {
}
