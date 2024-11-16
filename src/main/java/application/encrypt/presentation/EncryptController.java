package application.encrypt.presentation;

import application.auth.Auth;
import application.encrypt.application.EncryptService;
import application.encrypt.application.result.DecryptResult;
import application.encrypt.domain.FileMetadata;
import application.encrypt.presentation.request.DecryptRequestedFileRequest;
import application.encrypt.presentation.request.DecryptSavedFileRequest;
import application.encrypt.presentation.request.FileEncryptWithSaveRequest;
import application.encrypt.presentation.request.FileEncryptWithoutSaveRequest;
import application.member.domain.Member;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/files")
public class EncryptController {

    private final EncryptService encryptService;

    @PostMapping("/encrypt")
    public ResponseEntity<Long> encrypt(
            @Auth Member member,
            @ModelAttribute FileEncryptWithSaveRequest request
    ) throws Exception {
        Long memberId = member.getId();
        FileMetadata metadata = encryptService.encrypt(request.toCommand(memberId));
        return ResponseEntity.ok(metadata.getId());
    }

    @PostMapping("/encrypt-with-no-save")
    public ResponseEntity<byte[]> encryptWithNoSave(
            @ModelAttribute FileEncryptWithoutSaveRequest request
    ) throws Exception {
        byte[] bytes = encryptService.encryptWithoutSave(request.toCommand());
        return writeFile(bytes, "encrypted_" + request.file().getOriginalFilename());
    }

    @PostMapping("/decrypt/{fileId}")
    public ResponseEntity<byte[]> decrypt(
            @Auth Member member,
            @PathVariable("fileId") Long fileId,
            @RequestBody DecryptSavedFileRequest request
    ) throws Exception {
        DecryptResult decryptResult = encryptService.decryptSavedFile(request.toCommand(fileId, member));
        byte[] decryptedBytes = decryptResult.decryptedByte();
        return writeFile(decryptedBytes, "decrypted_" + decryptResult.metadata().getFileName());
    }

    @PostMapping("/decrypt")
    public ResponseEntity<byte[]> decrypt(
            @ModelAttribute DecryptRequestedFileRequest request
    ) throws Exception {
        byte[] bytes = encryptService.decryptRequestedFile(request.toCommand());
        return writeFile(bytes, "decrypted_" + request.file().getOriginalFilename());
    }

    private ResponseEntity<byte[]> writeFile(byte[] bytes, String fileName) throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        outputStream.write(bytes);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        headers.setContentDispositionFormData("attachment", fileName);
        return new ResponseEntity<>(outputStream.toByteArray(), headers, HttpStatus.OK);
    }
}
