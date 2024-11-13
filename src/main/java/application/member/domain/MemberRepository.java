package application.member.domain;

public interface MemberRepository {

    boolean existsByUsername(String username);

    Member save(Member member);

    Member getByUsername(String username);
}
