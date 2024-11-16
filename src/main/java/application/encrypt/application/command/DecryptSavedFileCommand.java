package application.encrypt.application.command;

public record DecryptSavedFileCommand(
        Long id,
        Long memberId,
        String folderKey
) {
}
