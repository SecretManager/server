package application.encrypt.persistence.metadata;

import application.common.NotFoundException;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FileMetadataEntityRepository extends JpaRepository<FileMetadataEntity, Long> {

    Optional<FileMetadataEntity> findByIdAndMemberId(Long id, Long memberId);

    default FileMetadataEntity getByIdAndMemberId(Long id, Long memberId) {
        return findByIdAndMemberId(id, memberId)
                .orElseThrow(() -> new NotFoundException("파일을 찾을 수 없습니다. (파일이 없거나 권한이 존재하지 않습니다.)"));
    }
}
