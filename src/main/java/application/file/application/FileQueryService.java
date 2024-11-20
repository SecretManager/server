package application.file.application;

import application.file.domain.FileMetadata;
import application.file.domain.FileMetadataRepository;
import application.member.domain.Member;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class FileQueryService {

    private final FileMetadataRepository fileMetadataRepository;

    public List<FileMetadata> findAllByMemberAndNameContains(Member member, String name) {
        return fileMetadataRepository.findAllByMemberAndNameContains(member, name);
    }

    public List<FileMetadata> findKeyHints(Member member, List<Long> fileIds) {
        return fileMetadataRepository.findAllByMemberAndFileIds(member, fileIds);
    }
}
