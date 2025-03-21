package application.file.domain.key;

import application.member.domain.Member;

public interface PersonalKeyRepository {

    PersonalKey save(PersonalKey personalKey);

    PersonalKey getByMember(Member member);
}
