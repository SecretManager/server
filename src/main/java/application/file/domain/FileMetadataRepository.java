package application.file.domain;

import application.member.domain.Member;

public interface FileMetadataRepository {

    FileMetadata save(FileMetadata fileMetadata);

    FileMetadata getByIdAndMember(Long id, Member member);
}
