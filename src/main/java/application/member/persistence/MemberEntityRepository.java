package application.member.persistence;

import application.common.NotFoundException;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberEntityRepository extends JpaRepository<MemberEntity, Long> {

    boolean existsByUsername(String username);

    Optional<MemberEntity> findByUsername(String username);

    default MemberEntity getByUsername(String username) {
        return findByUsername(username)
                .orElseThrow(() -> new NotFoundException("username 을 가진 회원을 조회할 수 없습니다."));
    }
}
