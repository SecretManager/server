package application.encrypt.persistence.metadata;

import application.encrypt.domain.FileMetadata;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface FileMetadataPersistenceMapper {

    FileMetadata toDomain(FileMetadataEntity entity);

    FileMetadataEntity toEntity(FileMetadata metadata);
}
