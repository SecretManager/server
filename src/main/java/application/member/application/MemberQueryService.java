package application.member.application;

import application.member.domain.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class MemberQueryService {

    private final MemberRepository memberRepository;

    public boolean checkDuplicatedUsername(String username) {
        return memberRepository.existsByUsername(username);
    }
}
