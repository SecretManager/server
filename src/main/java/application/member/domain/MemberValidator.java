package application.member.domain;

import application.common.exception.ConflictException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class MemberValidator {

    private final MemberRepository memberRepository;

    public void validateSignup(Member member) {
        if (memberRepository.existsByUsername(member.getUsername())) {
            throw new ConflictException("아이디가 중복되었습니다. 다른 아이디를 사용해주세요.");
        }

        if (memberRepository.existsByEmail(member.getEmail())) {
            throw new ConflictException("아이디가 중복되었습니다. 다른 아이디를 사용해주세요.");
        }
    }
}
