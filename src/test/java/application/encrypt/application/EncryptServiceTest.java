package application.encrypt.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

import application.common.exception.ForbiddenException;
import application.encrypt.application.command.DecryptRequestedFileCommand;
import application.encrypt.application.command.DecryptSavedFileCommand;
import application.encrypt.application.command.EncryptWithSaveCommand;
import application.encrypt.application.command.EncryptWithoutSaveCommand;
import application.encrypt.application.result.DecryptResult;
import application.encrypt.domain.FileMetadata;
import application.encrypt.domain.key.FolderKey;
import application.member.application.command.SignupCommand;
import application.support.IntegrationTest;
import java.nio.charset.StandardCharsets;
import java.util.Optional;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

@DisplayName("암호화 서비스 (EncryptService) 은(는)")
@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(ReplaceUnderscores.class)
class EncryptServiceTest extends IntegrationTest {

    private Long memberId;

    @BeforeEach
    void setUp() {
        memberId = memberService.signup(new SignupCommand("username", "1234", Optional.empty()))
                .getId();
    }

    @Test
    void 회원용_암호화() {
        // given
        String target = "target";
        String fileName = "fileName";
        FolderKey folderKey = FolderKey.ofPlainKeyForEncrypt("secret", null);
        byte[] bytes = target.getBytes(StandardCharsets.UTF_8);
        EncryptWithSaveCommand command = new EncryptWithSaveCommand(memberId, fileName, "", folderKey, bytes);

        // when & then
        assertDoesNotThrow(() -> {
            encryptService.encrypt(command);
        });
    }

    @Test
    void 저장_없이_암호화() {
        // given
        String target = "target";
        FolderKey folderKey = FolderKey.ofPlainKeyForEncrypt("secret", null);
        byte[] bytes = target.getBytes(StandardCharsets.UTF_8);
        EncryptWithoutSaveCommand command = new EncryptWithoutSaveCommand(folderKey, bytes);

        // when & then
        assertDoesNotThrow(() -> {
            encryptService.encryptWithoutSave(command);
        });
    }

    @Test
    void 저장된_파일_복호화() {
        // given
        String target = "target";
        String fileName = "fileName";
        FolderKey folderKey = FolderKey.ofPlainKeyForEncrypt("secret", null);
        byte[] bytes = target.getBytes(StandardCharsets.UTF_8);
        EncryptWithSaveCommand saveCommand = new EncryptWithSaveCommand(memberId, fileName, "", folderKey, bytes);
        ArgumentCaptor<byte[]> uploadByteCapture = ArgumentCaptor.forClass(byte[].class);
        FileMetadata metadata = encryptService.encrypt(saveCommand);
        verify(s3ApiClient).uploadByteUsingStream(uploadByteCapture.capture(), anyString());

        DecryptSavedFileCommand command = new DecryptSavedFileCommand(metadata.getId(), memberId, folderKey);
        given(s3ApiClient.downloadByteFile(eq(metadata.getEncryptedFileName())))
                .willReturn(uploadByteCapture.getValue());

        // when
        DecryptResult decrypt = encryptService.decryptSavedFile(command);

        // then
        String result = new String(decrypt.decryptedByte(), StandardCharsets.UTF_8);
        assertThat(result).isEqualTo(target);
    }

    @Test
    void 저장_없이_사용자용_복호화() {
        // given
        String target = "target";
        String fileName = "fileName";
        FolderKey folderKey = FolderKey.ofPlainKeyForEncrypt("secret", null);
        byte[] bytes = target.getBytes(StandardCharsets.UTF_8);
        EncryptWithoutSaveCommand command = new EncryptWithoutSaveCommand(folderKey, bytes);
        byte[] encrypted = encryptService.encryptWithoutSave(command);

        // when
        byte[] decrypted = encryptService.decryptRequestedFile(new DecryptRequestedFileCommand(encrypted, folderKey));

        // then
        String result = new String(decrypted, StandardCharsets.UTF_8);
        assertThat(result).isEqualTo(target);
    }

    @Test
    void 시크릿_키_오류로_파일_복호화_실패() {
        // given
        String target = "target";
        String fileName = "fileName";
        FolderKey folderKey = FolderKey.ofPlainKeyForEncrypt("secret", null);
        byte[] bytes = target.getBytes(StandardCharsets.UTF_8);
        EncryptWithSaveCommand saveCommand = new EncryptWithSaveCommand(memberId, fileName, "", folderKey, bytes);
        ArgumentCaptor<byte[]> uploadByteCapture = ArgumentCaptor.forClass(byte[].class);
        FileMetadata metadata = encryptService.encrypt(saveCommand);
        verify(s3ApiClient).uploadByteUsingStream(uploadByteCapture.capture(), anyString());

        FolderKey failFolderKey = FolderKey.ofPlainKeyForEncrypt("secret1", null);
        DecryptSavedFileCommand command = new DecryptSavedFileCommand(metadata.getId(), memberId, failFolderKey);
        given(s3ApiClient.downloadByteFile(eq(metadata.getEncryptedFileName())))
                .willReturn(uploadByteCapture.getValue());

        // when & then
        Assertions.assertThatThrownBy(() -> {
            encryptService.decryptSavedFile(command);
        }).isInstanceOf(ForbiddenException.class);
    }
}
