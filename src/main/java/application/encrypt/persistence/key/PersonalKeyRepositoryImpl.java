package application.encrypt.persistence.key;

import application.encrypt.domain.key.PersonalKey;
import application.encrypt.domain.key.PersonalKeyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class PersonalKeyRepositoryImpl implements PersonalKeyRepository {

    private final PersonalKeyEntityRepository personalKeyEntityRepository;
    private final PersonalKeyPersistenceMapper mapper;

    @Override
    public PersonalKey save(PersonalKey personalKey) {
        PersonalKeyEntity entity = mapper.toEntity(personalKey);
        PersonalKeyEntity saved = personalKeyEntityRepository.save(entity);
        return mapper.toDomain(saved);
    }
    
    @Override
    public PersonalKey getByMemberId(Long memberId) {
        PersonalKeyEntity entity = personalKeyEntityRepository.getByMemberId(memberId);
        return mapper.toDomain(entity);
    }
}
