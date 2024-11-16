package application.encrypt.application;

import application.encrypt.domain.key.DefaultFolderKey;
import application.encrypt.domain.key.DefaultFolderKeyRepository;
import application.encrypt.domain.key.PersonalKey;
import application.encrypt.domain.key.PersonalKeyRepository;
import application.member.domain.MemberSignupEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class KeyService {

    private final DefaultFolderKeyRepository defaultFolderKeyRepository;
    private final PersonalKeyRepository personalKeyRepository;

    @EventListener(MemberSignupEvent.class)
    public void createMemberKey(MemberSignupEvent event) {
        Long memberId = event.memberId();
        log.info("회원가입 이벤트를 받아 회원 키 생성 시도. memberId: {}", memberId);

        DefaultFolderKey defaultFolderKey = new DefaultFolderKey(memberId);
        PersonalKey personalKey = PersonalKey.createRandom(memberId);
        DefaultFolderKey savedFolderKey = defaultFolderKeyRepository.save(defaultFolderKey);
        PersonalKey savedPersonalKey = personalKeyRepository.save(personalKey);

        log.info("회원 키 생성 완료. DefaultFolderKeyId: {},  PersonalKeyId: {}",
                savedFolderKey.getId(), savedPersonalKey.id());
    }
}
