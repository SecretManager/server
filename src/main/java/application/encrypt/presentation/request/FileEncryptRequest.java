package application.encrypt.presentation.request;

import application.encrypt.application.command.FileEncryptCommand;
import application.member.domain.Member;
import jakarta.annotation.Nullable;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
public record FileEncryptRequest(
        MultipartFile file,
        @Nullable String plainKey,
        boolean downloadFile
) {
    public FileEncryptCommand toCommand(Member member) {
        try {
            return new FileEncryptCommand(member.getId(), file.getOriginalFilename(), plainKey, file.getBytes());
        } catch (Exception e) {
            log.info("Failed to get file's bytes");
            throw new RuntimeException(e);
        }
    }
}
