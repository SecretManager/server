package application.encrypt.presentation.request;

import application.encrypt.application.command.FileDecryptCommand;
import jakarta.annotation.Nullable;
import org.springframework.web.multipart.MultipartFile;

public record FileDecryptForRequestedFileRequest(
        MultipartFile file,
        @Nullable String plainKey
) {
    public FileDecryptCommand toCommand() {
        return null;
    }
}
