package application.file.application;

import application.file.domain.key.PersonalKey;
import application.file.domain.key.PersonalKeyRepository;
import application.member.domain.MemberSignupEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class KeyService {

    private final PersonalKeyRepository personalKeyRepository;

    @EventListener(MemberSignupEvent.class)
    public void createMemberKey(MemberSignupEvent event) {
        Long memberId = event.memberId();
        log.info("회원가입 이벤트를 받아 회원 키 생성 시도. memberId: {}", memberId);

        PersonalKey personalKey = PersonalKey.createRandom(memberId);
        PersonalKey savedPersonalKey = personalKeyRepository.save(personalKey);

        log.info("회원 키 생성 완료. PersonalKeyId: {}", savedPersonalKey.id());
    }
}
