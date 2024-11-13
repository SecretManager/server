package application.encrypt.application.result;

import application.encrypt.domain.FileMetadata;

public record EncryptResult(
        FileMetadata metadata,
        byte[] encryptedByte
) {
}
