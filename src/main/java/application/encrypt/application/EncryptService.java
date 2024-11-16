package application.encrypt.application;

import application.encrypt.application.command.DecryptRequestedFileCommand;
import application.encrypt.application.command.DecryptSavedFileCommand;
import application.encrypt.application.command.EncryptWithSaveCommand;
import application.encrypt.application.command.EncryptWithoutSaveCommand;
import application.encrypt.application.result.DecryptResult;
import application.encrypt.domain.Encryptor;
import application.encrypt.domain.FileMetadata;
import application.encrypt.domain.FileMetadataRepository;
import application.encrypt.domain.key.FolderKey;
import application.encrypt.domain.key.KeyChain;
import application.encrypt.domain.key.PersonalKey;
import application.encrypt.domain.key.PersonalKeyRepository;
import application.encrypt.domain.key.ServerKey;
import application.infra.s3.S3ApiClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class EncryptService {

    private final Encryptor encryptor;
    private final FileMetadataRepository fileMetadataRepository;
    private final S3ApiClient s3ApiClient;
    private final ServerKey serverKey;
    private final PersonalKeyRepository personalKeyRepository;

    public FileMetadata encrypt(EncryptWithSaveCommand command) {
        FolderKey folderKey = command.folderKey();
        PersonalKey personalKey = personalKeyRepository.getByMemberId(command.memberId());
        KeyChain keyChain = new KeyChain(folderKey, personalKey, serverKey);
        byte[] encrypt = encryptor.encrypt(command.bytes(), keyChain);
        FileMetadata metadata = FileMetadata.createRandomName(command.memberId(), command.fileName());
        s3ApiClient.uploadByteUsingStream(encrypt, metadata.getEncryptedFileName());
        return fileMetadataRepository.save(metadata);
    }

    public byte[] encryptWithoutSave(EncryptWithoutSaveCommand command) {
        FolderKey folderKey = command.folderKey();
        KeyChain keyChain = new KeyChain(folderKey, PersonalKey.none(), serverKey);
        return encryptor.encrypt(command.bytes(), keyChain);
    }

    public DecryptResult decryptSavedFile(DecryptSavedFileCommand command) {
        FileMetadata metadata = fileMetadataRepository.getByIdAndMemberId(command.id(), command.memberId());
        PersonalKey personalKey = personalKeyRepository.getByMemberId(command.memberId());
        FolderKey folderKey = command.folderKey();
        KeyChain keyChain = new KeyChain(folderKey, personalKey, serverKey);
        byte[] encrypted = s3ApiClient.downloadByteFile(metadata.getEncryptedFileName());
        byte[] decrypt = encryptor.decrypt(encrypted, keyChain);
        return new DecryptResult(metadata, decrypt);
    }

    public byte[] decryptRequestedFile(DecryptRequestedFileCommand command) {
        PersonalKey personalKey = PersonalKey.none();
        FolderKey folderKey = command.folderKey();
        KeyChain keyChain = new KeyChain(folderKey, personalKey, serverKey);
        return encryptor.decrypt(command.encrypted(), keyChain);
    }
}
