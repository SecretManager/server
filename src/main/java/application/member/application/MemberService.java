package application.member.application;

import application.member.application.command.LoginCommand;
import application.member.application.command.SignupCommand;
import application.member.application.command.UpdateProfileCommand;
import application.member.domain.Member;
import application.member.domain.MemberRepository;
import application.member.domain.MemberSignupEvent;
import application.member.domain.MemberValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class MemberService {

    private final MemberRepository memberRepository;
    private final MemberValidator memberValidator;
    private final ApplicationEventPublisher publisher;

    public Member signup(SignupCommand command) {
        Member member = command.toMember();
        member.signup(memberValidator);
        Member saved = memberRepository.save(member);
        publisher.publishEvent(new MemberSignupEvent(saved.getId()));
        return saved;
    }

    public Member login(LoginCommand command) {
        Member member = memberRepository.getByUsername(command.username());
        member.login(command.plainPassword());
        return member;
    }

    public void update(UpdateProfileCommand command) {
        Member member = command.member();
        member.update(command.name(), command.email());
        memberRepository.save(member);
    }
}
