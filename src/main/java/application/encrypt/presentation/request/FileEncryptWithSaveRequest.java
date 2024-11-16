package application.encrypt.presentation.request;

import application.common.utils.FileUtils;
import application.encrypt.application.command.EncryptWithSaveCommand;
import jakarta.annotation.Nullable;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
public record FileEncryptWithSaveRequest(
        MultipartFile file,
        String fileName,
        String description,
        @Nullable String folderKey,
        @Nullable String hint
) {
    public EncryptWithSaveCommand toCommand(
            Long memberId
    ) {
        try {
            return new EncryptWithSaveCommand(
                    memberId,
                    fileName + "." + FileUtils.getFileExtension(file),
                    description,
                    folderKey,
                    hint,
                    file.getBytes()
            );
        } catch (Exception e) {
            log.info("Failed to get file's bytes");
            throw new RuntimeException(e);
        }
    }
}
