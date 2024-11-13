package application.encrypt.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;

import application.common.ForbiddenException;
import application.encrypt.application.command.FileDecryptCommand;
import application.encrypt.application.command.FileEncryptCommand;
import application.encrypt.application.result.DecryptResult;
import application.encrypt.application.result.EncryptResult;
import application.support.IntegrationTest;
import java.nio.charset.StandardCharsets;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;

@DisplayName("암호화 서비스 (EncryptService) 은(는)")
@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(ReplaceUnderscores.class)
class EncryptServiceTest extends IntegrationTest {

    private final Long memberId = 1L;

    @BeforeEach
    void setUp() {
        memberEntityRepository.save(createMember(memberId));
    }

    @Test
    void 회원용_암호화() {
        // given
        String target = "target";
        String fileName = "fileName";
        String key = "secret";
        byte[] bytes = target.getBytes(StandardCharsets.UTF_8);
        FileEncryptCommand command = new FileEncryptCommand(memberId, fileName, key, bytes);

        // when & then
        assertDoesNotThrow(() -> {
            encryptService.encrypt(command);
        });
    }

    @Test
    void 미가입_사용자용_암호화() {
        // given
        String target = "target";
        String fileName = "fileName";
        String key = "secret";
        byte[] bytes = target.getBytes(StandardCharsets.UTF_8);
        FileEncryptCommand command = new FileEncryptCommand(null, fileName, key, bytes);

        // when & then
        assertDoesNotThrow(() -> {
            encryptService.encryptForUnAuthorized(command);
        });
    }

    @Test
    void 회원용_복호화() {
        // given
        String target = "target";
        String fileName = "fileName";
        String key = "secret";
        byte[] bytes = target.getBytes(StandardCharsets.UTF_8);
        EncryptResult encrypt = encryptService.encrypt(new FileEncryptCommand(memberId, fileName, key, bytes));
        FileDecryptCommand command = new FileDecryptCommand(encrypt.metadata().getId(), memberId, key);
        given(s3ApiClient.downloadByteFile(eq(encrypt.metadata().getEncryptedFileName())))
                .willReturn(encrypt.encryptedByte());

        // when
        DecryptResult decrypt = encryptService.decrypt(command);

        // then
        String result = new String(decrypt.decryptedByte(), StandardCharsets.UTF_8);
        assertThat(result).isEqualTo(target);
    }

    @Test
    void 미가입_사용자용_복호화() {
        // given
        String target = "target";
        String fileName = "fileName";
        String key = "secret";
        byte[] bytes = target.getBytes(StandardCharsets.UTF_8);
        FileEncryptCommand command = new FileEncryptCommand(null, fileName, key, bytes);
        EncryptResult encrypt = encryptService.encryptForUnAuthorized(command);

        // when
        byte[] decrypted = encryptService.decryptForUnAuthorized(encrypt.encryptedByte(), key)
                .decryptedByte();

        // then
        String result = new String(decrypted, StandardCharsets.UTF_8);
        assertThat(result).isEqualTo(target);
    }

    @Test
    void 시크릿_키_오류로_파일_복호화_실패() {
        // given
        String target = "target";
        String fileName = "fileName";
        String key = "secret";
        byte[] bytes = target.getBytes(StandardCharsets.UTF_8);
        EncryptResult encrypt = encryptService.encrypt(new FileEncryptCommand(memberId, fileName, key, bytes));
        given(s3ApiClient.downloadByteFile(eq(encrypt.metadata().getEncryptedFileName())))
                .willReturn(encrypt.encryptedByte());
        FileDecryptCommand command = new FileDecryptCommand(encrypt.metadata().getId(), memberId, "wrong");

        // when & then
        Assertions.assertThatThrownBy(() -> {
            encryptService.decrypt(command);
        }).isInstanceOf(ForbiddenException.class);
    }
}
