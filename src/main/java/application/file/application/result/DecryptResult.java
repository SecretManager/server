package application.file.application.result;

import application.file.domain.FileMetadata;

public record DecryptResult(
        FileMetadata metadata,
        byte[] decryptedByte
) {
}
