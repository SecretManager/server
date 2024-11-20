package application.member.domain;

public interface MemberRepository {

    boolean existsByUsername(String username);

    Member save(Member member);

    Member getByUsername(String username);

    Member getById(Long memberId);

    boolean existsByEmail(String email);
}
