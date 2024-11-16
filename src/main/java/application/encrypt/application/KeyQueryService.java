package application.encrypt.application;

import application.encrypt.application.command.FolderKeyQuery;
import application.encrypt.domain.key.DefaultFolderKeyRepository;
import application.encrypt.domain.key.FolderKey;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class KeyQueryService {

    private final DefaultFolderKeyRepository defaultFolderKeyRepository;

    public FolderKey getFolderKeyForEncrypt(FolderKeyQuery query) {
        if (query.useDefaultFolderKey()) {
            return defaultFolderKeyRepository.getByMemberId(query.memberId())
                    .toFolderKey();
        }
        return FolderKey.ofPlainKeyForEncrypt(query.folderKey(), query.hint());
    }
}
