package application.encrypt.application.command;

import java.io.InputStream;

public record FileEncryptCommand(
        Long memberId,
        String fileName,
        String plainKey,
        byte[] bytes
) {
}
