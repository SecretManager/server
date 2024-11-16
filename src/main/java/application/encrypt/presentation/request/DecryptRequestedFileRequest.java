package application.encrypt.presentation.request;

import application.encrypt.application.command.DecryptRequestedFileCommand;
import application.encrypt.domain.key.FolderKey;
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
            return new DecryptRequestedFileCommand(file.getBytes(), FolderKey.fromPlainKeyForDecrypt(folderKey));
        } catch (Exception e) {
            log.info("Failed to get file's bytes");
            throw new RuntimeException(e);
        }
    }
}
