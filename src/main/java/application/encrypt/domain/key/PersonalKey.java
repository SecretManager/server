package application.encrypt.domain.key;

import java.util.UUID;

public record PersonalKey(
        Long id,
        Long memberId,
        String uuid
) implements Key {
    public static PersonalKey createRandom(Long memberId) {
        return new PersonalKey(null, memberId, UUID.randomUUID().toString());
    }

    public static PersonalKey none() {
        return new PersonalKey(null, null, null);
    }

    @Override
    public String getKey() {
        return uuid;
    }
}
