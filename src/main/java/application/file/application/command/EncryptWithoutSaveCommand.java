package application.file.application.command;

public record EncryptWithoutSaveCommand(
        String folderKey,
        String hint,
        byte[] bytes
) {
}
