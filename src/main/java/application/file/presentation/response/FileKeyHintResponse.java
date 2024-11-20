package application.file.presentation.response;

import application.file.domain.FileMetadata;

public record FileKeyHintResponse(
        Long fileId,
        String hint
) {
    public static FileKeyHintResponse from(FileMetadata fileMetadata) {
        return new FileKeyHintResponse(
                fileMetadata.getId(),
                fileMetadata.getKeyHint()
        );
    }
}
