package application.file.persistence.metadata;

import application.file.domain.FileMetadata;
import application.file.domain.FileMetadataRepository;
import application.member.domain.Member;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class FileMetadataRepositoryImpl implements FileMetadataRepository {

    private final FileMetadataEntityRepository fileMetadataRepository;
    private final FileMetadataPersistenceMapper mapper;

    @Override
    public FileMetadata save(FileMetadata fileMetadata) {
        FileMetadataEntity entity = mapper.toEntity(fileMetadata);
        FileMetadataEntity saved = fileMetadataRepository.save(entity);
        return mapper.toDomain(saved);
    }

    @Override
    public FileMetadata getByIdAndMember(Long id, Member member) {
        FileMetadataEntity found = fileMetadataRepository.getByIdAndMemberId(id, member.getId());
        return mapper.toDomain(found);
    }

    @Override
    public List<FileMetadata> findAllByMemberAndNameContains(Member member, String name) {
        if (name == null || name.isBlank()) {
            return fileMetadataRepository.findAllByMemberId(member.getId()).stream()
                    .map(mapper::toDomain)
                    .toList();
        }
        return fileMetadataRepository.findAllByMemberIdAndNameContains(member.getId(), name)
                .stream()
                .map(mapper::toDomain)
                .toList();
    }

    @Override
    public List<FileMetadata> findAllByMemberAndFileIds(Member member, List<Long> fileIds) {
        return fileMetadataRepository.findByIdsInAndMemberId(fileIds, member.getId())
                .stream()
                .map(mapper::toDomain)
                .toList();
    }
}
