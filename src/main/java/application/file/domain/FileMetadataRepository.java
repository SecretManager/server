package application.file.domain;

import application.member.domain.Member;
import java.util.List;

public interface FileMetadataRepository {

    FileMetadata save(FileMetadata fileMetadata);

    FileMetadata getByIdAndMember(Long id, Member member);

    List<FileMetadata> findAllByMemberAndNameContains(Member member, String name);

    List<FileMetadata> findAllByMemberAndFileIds(Member member, List<Long> fileIds);
}
