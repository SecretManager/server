package application.file.presentation.request;

import application.file.application.command.DecryptRequestedFileCommand;
import jakarta.annotation.Nullable;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
public record DecryptRequestedFileRequest(
        MultipartFile file,
        @Nullable String folderKey
) {
    public DecryptRequestedFileCommand toCommand() {
        try {
            return new DecryptRequestedFileCommand(file.getBytes(), folderKey);
        } catch (Exception e) {
            log.info("Failed to get file's bytes");
            throw new RuntimeException(e);
        }
    }
}
