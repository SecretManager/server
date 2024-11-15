package application.member.application;

import application.member.application.command.LoginCommand;
import application.member.application.command.SignupCommand;
import application.member.domain.Member;
import application.member.domain.MemberRepository;
import application.member.domain.MemberValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class MemberService {

    private final MemberRepository memberRepository;
    private final MemberValidator memberValidator;

    public Member signup(SignupCommand command) {
        Member member = command.toMember();
        member.signup(memberValidator);
        return memberRepository.save(member);
    }

    public Member login(LoginCommand command) {
        Member member = memberRepository.getByUsername(command.username());
        member.login(command.plainPassword());
        return member;
    }
}
