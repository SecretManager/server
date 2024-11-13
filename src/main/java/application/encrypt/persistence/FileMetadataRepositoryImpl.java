package application.encrypt.persistence;

import application.encrypt.domain.FileMetadata;
import application.encrypt.domain.FileMetadataRepository;
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
    public FileMetadata getByIdAndMemberId(Long id, Long memberId) {
        FileMetadataEntity found = fileMetadataRepository.getByIdAndMemberId(id, memberId);
        return mapper.toDomain(found);
    }
}
