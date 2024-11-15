package application.encrypt.presentation;

import application.auth.Auth;
import application.encrypt.application.EncryptService;
import application.encrypt.application.result.DecryptResult;
import application.encrypt.application.result.EncryptResult;
import application.encrypt.presentation.request.FileDecryptForRequestedFileRequest;
import application.encrypt.presentation.request.FileDecryptRequest;
import application.encrypt.presentation.request.FileEncryptRequest;
import application.encrypt.presentation.request.FileEncryptWithNoSaveRequest;
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
    public ResponseEntity<byte[]> encrypt(
            @Auth Member member,
            @ModelAttribute FileEncryptRequest request
    ) throws Exception {
        EncryptResult encryptResult = encryptService.encrypt(request.toCommand(member));
        if (request.downloadFile()) {
            String filename = encryptResult.metadata().getOriginalFileName();
            byte[] encrypt = encryptResult.encryptedByte();
            return writeFile(encrypt, "encrypted_" + filename);
        }
        return ResponseEntity.ok(null);
    }

    @PostMapping("/encrypt-with-no-save")
    public ResponseEntity<byte[]> encryptWithNoSave(
            @ModelAttribute FileEncryptWithNoSaveRequest request
    ) throws Exception {
        EncryptResult encryptResult = encryptService.encryptWithoutSave(request.toCommand());
        String filename = encryptResult.metadata().getOriginalFileName();
        byte[] encrypt = encryptResult.encryptedByte();
        return writeFile(encrypt, "encrypted_" + filename);
    }

    @PostMapping("/decrypt")
    public ResponseEntity<byte[]> decrypt(
            @ModelAttribute FileDecryptForRequestedFileRequest request
    ) throws Exception {
        byte[] bytes = request.file().getBytes();
        DecryptResult decryptResult = encryptService.decryptForRequestedFile(bytes, request.plainKey());
        byte[] decryptedBytes = decryptResult.decryptedByte();
        return writeFile(decryptedBytes, "decrypted_" + request.file().getOriginalFilename());
    }

    @PostMapping("/decrypt/{fileId}")
    public ResponseEntity<byte[]> decrypt(
            @Auth Member member,
            @PathVariable("fileId") Long fileId,
            @RequestBody FileDecryptRequest request
    ) throws Exception {
        DecryptResult decryptResult = encryptService.decrypt(request.toCommand(fileId, member));
        byte[] decryptedBytes = decryptResult.decryptedByte();
        return writeFile(decryptedBytes, "decrypted_" + decryptResult.metadata().getOriginalFileName());
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
