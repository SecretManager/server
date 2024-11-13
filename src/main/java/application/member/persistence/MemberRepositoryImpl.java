package application.member.persistence;

import application.member.domain.Member;
import application.member.domain.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class MemberRepositoryImpl implements MemberRepository {

    private final MemberEntityRepository memberRepository;
    private final MemberPersistenceMapper mapper;

    @Override
    public boolean existsByUsername(String username) {
        return memberRepository.existsByUsername(username);
    }

    @Override
    public Member save(Member member) {
        MemberEntity entity = mapper.toEntity(member);
        MemberEntity saved = memberRepository.save(entity);
        return mapper.toDomain(saved);
    }

    @Override
    public Member getByUsername(String username) {
        MemberEntity entity = memberRepository.getByUsername(username);
        return mapper.toDomain(entity);
    }
}
