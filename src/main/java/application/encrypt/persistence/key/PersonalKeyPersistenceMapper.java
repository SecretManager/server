package application.encrypt.persistence.key;

import application.encrypt.domain.key.PersonalKey;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PersonalKeyPersistenceMapper {

    PersonalKey toDomain(PersonalKeyEntity entity);

    PersonalKeyEntity toEntity(PersonalKey domain);
}
