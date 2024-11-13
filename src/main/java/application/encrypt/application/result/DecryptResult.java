package application.encrypt.application.result;

import application.encrypt.domain.FileMetadata;

public record DecryptResult(
        FileMetadata metadata,
        byte[] decryptedByte
) {
}
