package application.encrypt.domain.key;

import application.common.Default;
import lombok.Getter;

@Getter
public class DefaultFolderKey {

    private Long id;
    private Long memberId;
    private String hashedDefaultFolderKey;
    private String hint;

    public DefaultFolderKey(Long memberId) {
        this(null, memberId, null, null);
    }

    @Default
    public DefaultFolderKey(Long id, Long memberId, String hashedDefaultFolderKey, String hint) {
        this.id = id;
        this.memberId = memberId;
        this.hashedDefaultFolderKey = hashedDefaultFolderKey;
        this.hint = hint;
    }

    public FolderKey toFolderKey() {
        return new FolderKey(hashedDefaultFolderKey, hint);
    }
}
