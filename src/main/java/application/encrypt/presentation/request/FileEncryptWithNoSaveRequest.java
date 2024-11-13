package application.encrypt.presentation.request;

import application.encrypt.application.command.FileEncryptCommand;
import jakarta.annotation.Nullable;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
public record FileEncryptWithNoSaveRequest(
        MultipartFile file,
        @Nullable String plainKey
) {
    public FileEncryptCommand toCommand() {
        try {
            return new FileEncryptCommand(null, file.getOriginalFilename(), plainKey, file.getBytes());
        } catch (Exception e) {
            log.info("Failed to get file's bytes");
            throw new RuntimeException(e);
        }
    }
}
