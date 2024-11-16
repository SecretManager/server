package application.encrypt.domain.key;

public interface PersonalKeyRepository {

    PersonalKey save(PersonalKey personalKey);

    PersonalKey getByMemberId(Long memberId);
}
