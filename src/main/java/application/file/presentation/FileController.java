package application.file.presentation;

import application.auth.Auth;
import application.file.application.FileService;
import application.file.application.result.DecryptResult;
import application.file.domain.FileMetadata;
import application.file.presentation.request.DecryptRequestedFileRequest;
import application.file.presentation.request.DecryptSavedFileRequest;
import application.file.presentation.request.FileEncryptWithSaveRequest;
import application.file.presentation.request.FileEncryptWithoutSaveRequest;
import application.member.domain.Member;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/files")
public class FileController {

    private final FileService fileService;

    @PostMapping("/encrypt")
    public ResponseEntity<Long> encrypt(
            @Auth Member member,
            @ModelAttribute FileEncryptWithSaveRequest request
    ) throws Exception {
        FileMetadata metadata = fileService.encrypt(request.toCommand(member));
        return ResponseEntity.ok(metadata.getId());
    }

    @PostMapping("/encrypt-with-no-save")
    public ResponseEntity<byte[]> encryptWithNoSave(
            @ModelAttribute FileEncryptWithoutSaveRequest request
    ) throws Exception {
        byte[] bytes = fileService.encryptWithoutSave(request.toCommand());
        return writeFile(bytes, "encrypted_" + request.file().getOriginalFilename());
    }

    @PostMapping("/decrypt/{fileId}")
    public ResponseEntity<byte[]> decrypt(
            @Auth Member member,
            @PathVariable("fileId") Long fileId,
            @RequestBody DecryptSavedFileRequest request
    ) throws Exception {
        DecryptResult decryptResult = fileService.decryptSavedFile(request.toCommand(fileId, member));
        byte[] decryptedBytes = decryptResult.decryptedByte();
        return writeFile(decryptedBytes, "decrypted_" + decryptResult.metadata().getFileName());
    }

    @PostMapping("/decrypt")
    public ResponseEntity<byte[]> decrypt(
            @ModelAttribute DecryptRequestedFileRequest request
    ) throws Exception {
        byte[] bytes = fileService.decryptRequestedFile(request.toCommand());
        return writeFile(bytes, "decrypted_" + request.file().getOriginalFilename());
    }

    @GetMapping("/download/{fileId}")
    public ResponseEntity<byte[]> downloadEncryptedFile(
            @Auth Member member,
            @PathVariable("fileId") Long fileId
    ) throws IOException {
        DecryptResult decryptResult = fileService.downloadEncryptedFile(fileId, member);
        return writeFile(decryptResult.decryptedByte(), decryptResult.metadata().getFileName());
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
