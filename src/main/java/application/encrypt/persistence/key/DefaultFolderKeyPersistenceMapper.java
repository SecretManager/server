package application.encrypt.persistence.key;

import application.encrypt.domain.key.DefaultFolderKey;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface DefaultFolderKeyPersistenceMapper {

    DefaultFolderKey toDomain(DefaultFolderKeyEntity entity);

    DefaultFolderKeyEntity toEntity(DefaultFolderKey domain);
}
