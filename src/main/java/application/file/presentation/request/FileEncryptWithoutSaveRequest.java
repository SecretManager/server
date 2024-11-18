package application.file.presentation.request;

import application.file.application.command.EncryptWithoutSaveCommand;
import jakarta.annotation.Nullable;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
public record FileEncryptWithoutSaveRequest(
        MultipartFile file,
        @Nullable String folderKey,
        @Nullable String hint
) {
    public EncryptWithoutSaveCommand toCommand() {
        try {
            return new EncryptWithoutSaveCommand(folderKey, hint, file.getBytes());
        } catch (Exception e) {
            log.info("Failed to get file's bytes");
            throw new RuntimeException(e);
        }
    }
}
