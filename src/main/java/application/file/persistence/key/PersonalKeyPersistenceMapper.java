package application.file.persistence.key;

import application.file.domain.key.PersonalKey;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PersonalKeyPersistenceMapper {

    PersonalKey toDomain(PersonalKeyEntity entity);

    PersonalKeyEntity toEntity(PersonalKey domain);
}
