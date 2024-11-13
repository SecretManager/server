package application.encrypt.domain;

public interface FileMetadataRepository {

    FileMetadata save(FileMetadata fileMetadata);

    FileMetadata getByIdAndMemberId(Long id, Long memberId);
}
