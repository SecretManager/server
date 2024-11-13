package application.service;

import application.algorithm.Sha256;
import application.encryptor.Encryptor;
import application.encryptor.Nonce;
import java.io.InputStream;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class EncryptService {

  private final Encryptor encryptor;

  public byte[] encrypt(String fileName, String key, InputStream inputStream) {
    Nonce nonce = Nonce.generate();
    byte[] encrypt = encryptor.encrypt(inputStream, key, nonce);
    String generatedFileName = UUID.randomUUID().toString();
    String encryptKey = Sha256.encrypt(key);
    System.out.println("save filename, key with user id");
    return encrypt;
  }

  public void decrypt(Long fileId, String key) {
    System.out.println("get file from DB using fileId");
    System.out.println("get key ");
  }
}
