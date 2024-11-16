package application.encrypt.persistence.key;

import application.encrypt.domain.key.DefaultFolderKey;
import application.encrypt.domain.key.DefaultFolderKeyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class DefaultFolderKeyRepositoryImpl implements DefaultFolderKeyRepository {

    private final DefaultFolderKeyEntityRepository defaultFolderKeyEntityRepository;
    private final DefaultFolderKeyPersistenceMapper mapper;

    @Override
    public DefaultFolderKey save(DefaultFolderKey defaultFolderKey) {
        DefaultFolderKeyEntity entity = mapper.toEntity(defaultFolderKey);
        DefaultFolderKeyEntity saved = defaultFolderKeyEntityRepository.save(entity);
        return mapper.toDomain(saved);
    }

    @Override
    public DefaultFolderKey getByMemberId(Long memberId) {
        DefaultFolderKeyEntity entity = defaultFolderKeyEntityRepository.getByMemberId(memberId);
        return mapper.toDomain(entity);
    }
}
