package application.file.application.command;

public record DecryptRequestedFileCommand(
        byte[] encrypted, String folderKey
) {
}
