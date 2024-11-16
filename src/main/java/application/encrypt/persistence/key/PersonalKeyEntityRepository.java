package application.encrypt.persistence.key;

import application.common.exception.NotFoundException;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PersonalKeyEntityRepository extends JpaRepository<PersonalKeyEntity, Long> {

    Optional<PersonalKeyEntity> findByMemberId(Long memberId);

    default PersonalKeyEntity getByMemberId(Long memberId) {
        return findByMemberId(memberId).orElseThrow(
                () -> new NotFoundException("memberId가 %d인 회원의 개인 키가 없습니다.".formatted(memberId))
        );
    }
}
