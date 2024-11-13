package application.encrypt.application;

import application.encrypt.application.command.FileDecryptCommand;
import application.encrypt.application.command.FileEncryptCommand;
import application.encrypt.application.result.DecryptResult;
import application.encrypt.application.result.EncryptResult;
import application.encrypt.domain.Encryptor;
import application.encrypt.domain.FileMetadata;
import application.encrypt.domain.FileMetadataRepository;
import application.encrypt.domain.SecretKeyGenerator;
import application.encrypt.domain.ServerSecretKey;
import application.infra.s3.S3ApiClient;
import javax.crypto.SecretKey;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class EncryptService {

    private final Encryptor encryptor;
    private final SecretKeyGenerator secretKeyGenerator;
    private final FileMetadataRepository fileMetadataRepository;
    private final S3ApiClient s3ApiClient;
    private final ServerSecretKey serverSecretKey;

    public EncryptResult encryptForUnAuthorized(FileEncryptCommand command) {
        SecretKey keyForFile = secretKeyGenerator.generateAESKey(command.plainKey());
        byte[] encrypt = encryptor.encrypt(command.bytes(), keyForFile, serverSecretKey.getSecretKey());
        FileMetadata metadata = new FileMetadata(command.fileName());
        return new EncryptResult(metadata, encrypt);
    }

    public EncryptResult encrypt(FileEncryptCommand command) {
        Long memberId = command.memberId();
        SecretKey keyForFile = secretKeyGenerator.generateAESKey(command.plainKey());
        byte[] encrypt = encryptor.encrypt(command.bytes(), keyForFile, serverSecretKey.getSecretKey());
        FileMetadata metadata = FileMetadata.createRandomName(memberId, command.fileName());
        s3ApiClient.uploadByteUsingStream(encrypt, metadata.getEncryptedFileName());
        FileMetadata savedMetadata = fileMetadataRepository.save(metadata);
        return new EncryptResult(savedMetadata, encrypt);
    }

    public DecryptResult decrypt(FileDecryptCommand command) {
        FileMetadata metadata = fileMetadataRepository.getByIdAndMemberId(command.id(), command.memberId());
        byte[] encrypted = s3ApiClient.downloadByteFile(metadata.getEncryptedFileName());
        SecretKey secretKey = secretKeyGenerator.generateAESKey(command.plainKey());
        byte[] decrypt = encryptor.decrypt(encrypted, serverSecretKey.getSecretKey(), secretKey);
        return new DecryptResult(metadata, decrypt);
    }

    public DecryptResult decryptForUnAuthorized(byte[] encrypted, String key) {
        SecretKey secretKey = secretKeyGenerator.generateAESKey(key);
        byte[] decrypt = encryptor.decrypt(encrypted, serverSecretKey.getSecretKey(), secretKey);
        return new DecryptResult(null, decrypt);
    }
}
