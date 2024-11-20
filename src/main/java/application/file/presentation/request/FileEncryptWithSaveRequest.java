package application.file.presentation.request;

import application.file.application.command.EncryptWithSaveCommand;
import application.member.domain.Member;
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
            Member member
    ) {
        try {
            return new EncryptWithSaveCommand(
                    member,
                    fileName,
                    file.getSize(),
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
