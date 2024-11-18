package application.file.domain.key;

import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;
import javax.crypto.SecretKey;

public record KeyChain(
        FolderKey folderKey,
        PersonalKey personalKey,
        ServerKey serverKey
) {
    public List<SecretKey> getEncryptKeys(SecretKeyGenerator generator) {
        return Stream.of(serverKey, folderKey, personalKey)
                .filter(Objects::nonNull)
                .map(Key::getKey)
                .filter(Objects::nonNull)
                .map(generator::generateAESKey)
                .toList();
    }

    public List<SecretKey> getDecryptKeys(SecretKeyGenerator generator) {
        return getEncryptKeys(generator).reversed();
    }
}
