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
import application.member.domain.Member;
import application.member.domain.MemberRepository;
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
    private final MemberRepository memberRepository;

    public FileMetadata encrypt(EncryptWithSaveCommand command) {
        Member member = command.member();
        FolderKey folderKey = FolderKey.ofPlainKeyForEncrypt(command.folderKey(), command.hint());
        PersonalKey personalKey = personalKeyRepository.getByMember(command.member());
        KeyChain keyChain = new KeyChain(folderKey, personalKey, serverKey);

        byte[] encrypt = encryptor.encrypt(command.bytes(), keyChain);

        FileMetadata metadata = command.toFileMetadata();

        metadata.upload(member);
        s3ApiClient.uploadByteUsingStream(encrypt, metadata.getEncryptedFileName());

        memberRepository.save(member);
        return fileMetadataRepository.save(metadata);
    }

    public byte[] encryptWithoutSave(EncryptWithoutSaveCommand command) {
        FolderKey folderKey = FolderKey.ofPlainKeyForEncrypt(command.folderKey(), command.hint());
        KeyChain keyChain = new KeyChain(folderKey, PersonalKey.none(), serverKey);
        return encryptor.encrypt(command.bytes(), keyChain);
    }

    public DecryptResult decryptSavedFile(DecryptSavedFileCommand command) {
        Member member = command.member();
        FileMetadata metadata = fileMetadataRepository.getByIdAndMember(command.id(), member);
        metadata.download(member);

        PersonalKey personalKey = personalKeyRepository.getByMember(member);
        FolderKey folderKey = FolderKey.fromPlainKeyForDecrypt(command.folderKey());
        KeyChain keyChain = new KeyChain(folderKey, personalKey, serverKey);

        byte[] encrypted = s3ApiClient.downloadByteFile(metadata.getEncryptedFileName());

        byte[] decrypt = encryptor.decrypt(encrypted, keyChain);

        memberRepository.save(member);
        fileMetadataRepository.save(metadata);
        return new DecryptResult(metadata, decrypt);
    }

    public byte[] decryptRequestedFile(DecryptRequestedFileCommand command) {
        PersonalKey personalKey = PersonalKey.none();
        FolderKey folderKey = FolderKey.fromPlainKeyForDecrypt(command.folderKey());
        KeyChain keyChain = new KeyChain(folderKey, personalKey, serverKey);
        return encryptor.decrypt(command.encrypted(), keyChain);
    }

    public DecryptResult downloadEncryptedFile(Long fileId, Member member) {
        FileMetadata metadata = fileMetadataRepository.getByIdAndMember(fileId, member);
        metadata.download(member);

        PersonalKey personalKey = personalKeyRepository.getByMember(member);
        KeyChain keyChain = new KeyChain(null, personalKey, null);

        byte[] encrypted = s3ApiClient.downloadByteFile(metadata.getEncryptedFileName());

        byte[] decryptUsingPersonalKey = encryptor.decrypt(encrypted, keyChain);

        memberRepository.save(member);
        fileMetadataRepository.save(metadata);
        return new DecryptResult(metadata, decryptUsingPersonalKey);
    }
}
