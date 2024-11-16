package application.encrypt.domain.key;

public interface DefaultFolderKeyRepository {

    DefaultFolderKey save(DefaultFolderKey defaultFolderKey);

    DefaultFolderKey getByMemberId(Long memberId);
}
