package application.member.application;

import application.member.application.command.LoginCommand;
import application.member.application.command.SignupCommand;
import application.member.domain.Member;
import application.member.domain.MemberRepository;
import application.member.domain.MemberValidator;
import application.member.domain.PasswordEncryptor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class MemberService {

    private final MemberRepository memberRepository;
    private final MemberValidator memberValidator;
    private final PasswordEncryptor passwordEncryptor;

    public Member signup(SignupCommand command) {
        Member member = command.toMember();
        member.signup(memberValidator, passwordEncryptor);
        return memberRepository.save(member);
    }

    public Member login(LoginCommand command) {
        Member member = memberRepository.getByUsername(command.username());
        member.login(passwordEncryptor, command.plainPassword());
        return member;
    }
}
