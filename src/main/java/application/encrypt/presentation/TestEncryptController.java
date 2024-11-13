package application.encrypt.presentation;

import application.encrypt.domain.Encryptor;
import application.encrypt.domain.Nonce;
import java.io.ByteArrayOutputStream;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RequiredArgsConstructor
@Slf4j
@RestController
public class TestEncryptController {

    private final Encryptor encryptor;

    @PostMapping("/encrypts")
    public ResponseEntity<byte[]> encrypt(
            @RequestParam("file") MultipartFile file
    ) throws Exception {
        String filename = file.getOriginalFilename();
        byte[] encrypt = encryptor.encrypt(file.getInputStream(), "server secret", Nonce.generate());

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        outputStream.write(encrypt);

        // 응답 설정
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        headers.setContentDispositionFormData("attachment", "encrypted_" + filename);
        return new ResponseEntity<>(outputStream.toByteArray(), headers, HttpStatus.OK);
    }

    @PostMapping("/decrypts")
    public ResponseEntity<byte[]> decrypt(
            @RequestParam("file") MultipartFile file
    ) throws Exception {
        String filename = file.getOriginalFilename();
        byte[] encrypt = encryptor.decrypt(file.getInputStream(), "server secret");

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        outputStream.write(encrypt);

        // 응답 설정
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        headers.setContentDispositionFormData("attachment", "decrypted_" + filename);
        return new ResponseEntity<>(outputStream.toByteArray(), headers, HttpStatus.OK);
    }
}
