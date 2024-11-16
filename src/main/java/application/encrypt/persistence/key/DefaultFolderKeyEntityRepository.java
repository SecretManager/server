package application.encrypt.persistence.key;

import application.common.NotFoundException;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DefaultFolderKeyEntityRepository extends JpaRepository<DefaultFolderKeyEntity, Long> {

    Optional<DefaultFolderKeyEntity> findByMemberId(Long memberId);

    default DefaultFolderKeyEntity getByMemberId(Long memberId) {
        return findByMemberId(memberId).orElseThrow(
                () -> new NotFoundException("memberId가 %d인 회원의 기본 키가 없습니다.".formatted(memberId))
        );
    }
}
