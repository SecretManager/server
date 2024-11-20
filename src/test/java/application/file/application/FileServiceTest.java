package application.file.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

import application.common.exception.ForbiddenException;
import application.file.application.command.DecryptRequestedFileCommand;
import application.file.application.command.DecryptSavedFileCommand;
import application.file.application.command.EncryptWithSaveCommand;
import application.file.application.command.EncryptWithoutSaveCommand;
import application.file.application.result.DecryptResult;
import application.file.domain.FileMetadata;
import application.member.application.command.SignupCommand;
import application.member.domain.Member;
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

@DisplayName("암호화 서비스 (FileService) 은(는)")
@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(ReplaceUnderscores.class)
class FileServiceTest extends IntegrationTest {

    private Member member;

    @BeforeEach
    void setUp() {
        member = memberService.signup(new SignupCommand("username", "1234", ""));
    }

    @Test
    void 회원용_암호화() {
        // given
        String target = "target";
        String fileName = "fileName";
        byte[] bytes = target.getBytes(StandardCharsets.UTF_8);
        EncryptWithSaveCommand command = new EncryptWithSaveCommand(member, fileName, 10, "", "secret", "", bytes);

        // when & then
        assertDoesNotThrow(() -> {
            fileService.encrypt(command);
        });
    }

    @Test
    void 저장_없이_암호화() {
        // given
        String target = "target";
        byte[] bytes = target.getBytes(StandardCharsets.UTF_8);
        EncryptWithoutSaveCommand command = new EncryptWithoutSaveCommand("secret", "", bytes);

        // when & then
        assertDoesNotThrow(() -> {
            fileService.encryptWithoutSave(command);
        });
    }

    @Test
    void 저장된_파일_복호화() {
        // given
        String target = "target";
        String fileName = "fileName";
        byte[] bytes = target.getBytes(StandardCharsets.UTF_8);
        EncryptWithSaveCommand saveCommand = new EncryptWithSaveCommand(member, fileName, 10, "", "secret", "", bytes);
        ArgumentCaptor<byte[]> uploadByteCapture = ArgumentCaptor.forClass(byte[].class);
        FileMetadata metadata = fileService.encrypt(saveCommand);
        verify(s3ApiClient).uploadByteUsingStream(uploadByteCapture.capture(), anyString());

        DecryptSavedFileCommand command = new DecryptSavedFileCommand(metadata.getId(), member, "secret");
        given(s3ApiClient.downloadByteFile(eq(metadata.getEncryptedFileName())))
                .willReturn(uploadByteCapture.getValue());

        // when
        DecryptResult decrypt = fileService.decryptSavedFile(command);

        // then
        String result = new String(decrypt.decryptedByte(), StandardCharsets.UTF_8);
        assertThat(result).isEqualTo(target);
    }

    @Test
    void 저장_없이_사용자용_복호화() {
        // given
        String target = "target";
        byte[] bytes = target.getBytes(StandardCharsets.UTF_8);
        EncryptWithoutSaveCommand command = new EncryptWithoutSaveCommand("secret", null, bytes);
        byte[] encrypted = fileService.encryptWithoutSave(command);

        // when
        byte[] decrypted = fileService.decryptRequestedFile(new DecryptRequestedFileCommand(encrypted, "secret"));

        // then
        String result = new String(decrypted, StandardCharsets.UTF_8);
        assertThat(result).isEqualTo(target);
    }

    @Test
    void 시크릿_키_오류로_파일_복호화_실패() {
        // given
        String target = "target";
        String fileName = "fileName";
        byte[] bytes = target.getBytes(StandardCharsets.UTF_8);
        EncryptWithSaveCommand saveCommand = new EncryptWithSaveCommand(member, fileName, 10, "", "secret", null,
                bytes);
        ArgumentCaptor<byte[]> uploadByteCapture = ArgumentCaptor.forClass(byte[].class);
        FileMetadata metadata = fileService.encrypt(saveCommand);
        verify(s3ApiClient).uploadByteUsingStream(uploadByteCapture.capture(), anyString());

        DecryptSavedFileCommand command = new DecryptSavedFileCommand(metadata.getId(), member, "secret1");
        given(s3ApiClient.downloadByteFile(eq(metadata.getEncryptedFileName())))
                .willReturn(uploadByteCapture.getValue());

        // when & then
        Assertions.assertThatThrownBy(() -> {
            fileService.decryptSavedFile(command);
        }).isInstanceOf(ForbiddenException.class);
    }
}
