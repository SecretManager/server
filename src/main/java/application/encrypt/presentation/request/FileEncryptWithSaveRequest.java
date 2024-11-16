package application.encrypt.presentation.request;

import application.common.FileUtils;
import application.encrypt.application.command.EncryptWithSaveCommand;
import application.encrypt.application.command.FolderKeyQuery;
import application.encrypt.domain.key.FolderKey;
import jakarta.annotation.Nullable;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
public record FileEncryptWithSaveRequest(
        MultipartFile file,
        String fileName,
        String description,
        boolean useDefaultFolderKey,
        @Nullable String folderKey,
        @Nullable String hint
) {
    public EncryptWithSaveCommand toCommand(
            Long memberId,
            FolderKey folderKey
    ) {
        try {
            return new EncryptWithSaveCommand(
                    memberId,
                    fileName + "." + FileUtils.getFileExtension(file),
                    description,
                    folderKey,
                    file.getBytes()
            );
        } catch (Exception e) {
            log.info("Failed to get file's bytes");
            throw new RuntimeException(e);
        }
    }

    public FolderKeyQuery toFolderKeyQuery(Long memberId) {
        return new FolderKeyQuery(memberId, useDefaultFolderKey, folderKey, hint);
    }
}
