package application.file.persistence.metadata;

import application.file.domain.FileMetadata;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface FileMetadataPersistenceMapper {

    FileMetadata toDomain(FileMetadataEntity entity);

    FileMetadataEntity toEntity(FileMetadata metadata);
}
