package application.file.presentation.response;

import application.file.domain.FileMetadata;

public record UploadedFileResponse(
        Long id,
        String fileName,
        long fileBytesSize,
        String description,
        int currentDownloadCountPerMonth
) {
    public static UploadedFileResponse from(FileMetadata fileMetadata) {
        return new UploadedFileResponse(
                fileMetadata.getId(),
                fileMetadata.getFileName(),
                fileMetadata.getFileBytesSize(),
                fileMetadata.getDescription(),
                fileMetadata.getCurrentDownloadCountPerMonth()
        );
    }
}
