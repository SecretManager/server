package application.encrypt.application.command;

public record DecryptRequestedFileCommand(
        byte[] encrypted, String folderKey
) {
}
