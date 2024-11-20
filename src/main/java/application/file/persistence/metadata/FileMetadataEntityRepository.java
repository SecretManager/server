package application.file.persistence.metadata;

import application.common.exception.NotFoundException;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface FileMetadataEntityRepository extends JpaRepository<FileMetadataEntity, Long> {

    Optional<FileMetadataEntity> findByIdAndMemberId(Long id, Long memberId);

    default FileMetadataEntity getByIdAndMemberId(Long id, Long memberId) {
        return findByIdAndMemberId(id, memberId)
                .orElseThrow(() -> new NotFoundException("파일을 찾을 수 없습니다. (파일이 없거나 권한이 존재하지 않습니다.)"));
    }

    List<FileMetadataEntity> findAllByMemberId(Long memberId);

    @Query("SELECT fm FROM FileMetadataEntity fm WHERE fm.memberId = :memberId AND fm.fileName LIKE CONCAT('%',:name,'%')")
    List<FileMetadataEntity> findAllByMemberIdAndNameContains(Long memberId, String name);

    @Query("SELECT fm FROM FileMetadataEntity fm WHERE fm.memberId = :memberId AND fm.id IN (:fileIds)")
    List<FileMetadataEntity> findByIdsInAndMemberId(List<Long> fileIds, Long memberId);
}
