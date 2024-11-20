package application.file.presentation;

import application.auth.Auth;
import application.file.application.FileQueryService;
import application.file.application.FileService;
import application.file.application.result.DecryptResult;
import application.file.domain.FileMetadata;
import application.file.presentation.request.DecryptRequestedFileRequest;
import application.file.presentation.request.DecryptSavedFileRequest;
import application.file.presentation.request.FileEncryptWithSaveRequest;
import application.file.presentation.request.FileEncryptWithoutSaveRequest;
import application.file.presentation.response.FileKeyHintResponse;
import application.file.presentation.response.UploadedFileResponse;
import application.member.domain.Member;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/files")
public class FileController {

    private final FileService fileService;
    private final FileQueryService fileQueryService;

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
        return writeFile(bytes);
    }

    @PostMapping("/decrypt/{fileId}")
    public ResponseEntity<byte[]> decrypt(
            @Auth Member member,
            @PathVariable("fileId") Long fileId,
            @RequestBody DecryptSavedFileRequest request
    ) throws Exception {
        DecryptResult decryptResult = fileService.decryptSavedFile(request.toCommand(fileId, member));
        byte[] decryptedBytes = decryptResult.decryptedByte();
        return writeFile(decryptedBytes);
    }

    @PostMapping("/decrypt")
    public ResponseEntity<byte[]> decrypt(
            @ModelAttribute DecryptRequestedFileRequest request
    ) throws Exception {
        byte[] bytes = fileService.decryptRequestedFile(request.toCommand());
        return writeFile(bytes);
    }

    @GetMapping("/download/{fileId}")
    public ResponseEntity<byte[]> downloadEncryptedFile(
            @Auth Member member,
            @PathVariable("fileId") Long fileId
    ) throws IOException {
        DecryptResult decryptResult = fileService.downloadEncryptedFile(fileId, member);
        return writeFile(decryptResult.decryptedByte());
    }

    private ResponseEntity<byte[]> writeFile(byte[] bytes) throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        outputStream.write(bytes);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        return new ResponseEntity<>(outputStream.toByteArray(), headers, HttpStatus.OK);
    }

    @GetMapping("/my")
    public ResponseEntity<List<UploadedFileResponse>> findAllMyUploadedFiles(
            @Auth Member member,
            @RequestParam(value = "name", defaultValue = "") String name
    ) {
        List<UploadedFileResponse> list = fileQueryService.findAllByMemberAndNameContains(member, name).stream()
                .map(UploadedFileResponse::from)
                .toList();
        return ResponseEntity.ok(list);
    }

    @GetMapping("/hints")
    public ResponseEntity<List<FileKeyHintResponse>> findKeyHintsForSelectedFiles(
            @Auth Member member,
            @RequestParam("fileIds") List<Long> fileIds
    ) {
        List<FileKeyHintResponse> response = fileQueryService.findKeyHints(member, fileIds)
                .stream()
                .map(FileKeyHintResponse::from)
                .toList();
        return ResponseEntity.ok(response);
    }
}
