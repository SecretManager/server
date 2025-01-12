package application.file.application;

import application.file.application.command.DecryptRequestedFileCommand;
import application.file.application.command.DecryptSavedFileCommand;
import application.file.application.command.EncryptWithSaveCommand;
import application.file.application.command.EncryptWithoutSaveCommand;
import application.file.application.result.DecryptResult;
import application.file.domain.Encryptor;
import application.file.domain.FileMetadata;
import application.file.domain.FileMetadataRepository;
import application.file.domain.key.FolderKey;
import application.file.domain.key.KeyChain;
import application.file.domain.key.PersonalKey;
import application.file.domain.key.PersonalKeyRepository;
import application.file.domain.key.ServerKey;
import application.infra.s3.S3ApiClient;
import application.member.domain.Member;
import application.member.domain.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class FileService {

    private final Encryptor encryptor;
    private final FileMetadataRepository fileMetadataRepository;
    private final S3ApiClient s3ApiClient;
    private final ServerKey serverKey;
    private final PersonalKeyRepository personalKeyRepository;
    private final MemberRepository memberRepository;

    public FileMetadata encrypt(EncryptWithSaveCommand command) {
        Member member = command.member();

        FolderKey folderKey = FolderKey.fromPlainKey(command.folderKey());
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
        FolderKey folderKey = FolderKey.fromPlainKey(command.folderKey());
        KeyChain keyChain = new KeyChain(folderKey, PersonalKey.none(), serverKey);
        return encryptor.encrypt(command.bytes(), keyChain);
    }

    public DecryptResult decryptSavedFile(DecryptSavedFileCommand command) {
        Member member = command.member();

        FileMetadata metadata = fileMetadataRepository.getByIdAndMember(command.id(), member);
        metadata.download(member);

        PersonalKey personalKey = personalKeyRepository.getByMember(member);
        FolderKey folderKey = FolderKey.fromPlainKey(command.folderKey());
        KeyChain keyChain = new KeyChain(folderKey, personalKey, serverKey);

        byte[] encrypted = s3ApiClient.downloadByteFile(metadata.getEncryptedFileName());

        byte[] decrypt = encryptor.decrypt(encrypted, keyChain);

        memberRepository.save(member);
        fileMetadataRepository.save(metadata);
        return new DecryptResult(metadata, decrypt);
    }

    public byte[] decryptRequestedFile(DecryptRequestedFileCommand command) {
        FolderKey folderKey = FolderKey.fromPlainKey(command.folderKey());
        PersonalKey personalKey = PersonalKey.none();
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

    public void delete(Long fileId, Member member) {
        FileMetadata metadata = fileMetadataRepository.getByIdAndMember(fileId, member);
        metadata.delete(member);

        s3ApiClient.deleteFile(metadata.getEncryptedFileName());
        fileMetadataRepository.delete(metadata);
        memberRepository.save(member);
    }
}
