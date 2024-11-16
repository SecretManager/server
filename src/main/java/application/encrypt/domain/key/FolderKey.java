package application.encrypt.domain.key;

import application.common.algorithm.Sha256;
import jakarta.annotation.Nullable;
import lombok.Getter;

@Getter
public class FolderKey implements Key {

    @Nullable
    private String hashedDefaultFolderKey;
    private String hint;

    public FolderKey(@Nullable String hashedDefaultFolderKey, @Nullable String hint) {
        this.hashedDefaultFolderKey = hashedDefaultFolderKey;
        this.hint = hint;
    }

    public static FolderKey ofPlainKeyForEncrypt(String plainKey, String hint) {
        if (plainKey == null) {
            return new FolderKey(null, null);
        }
        return new FolderKey(Sha256.encrypt(plainKey), hint);
    }

    public static FolderKey fromPlainKeyForDecrypt(String plainKey) {
        if (plainKey == null) {
            return new FolderKey(null, null);
        }
        return new FolderKey(Sha256.encrypt(plainKey), null);
    }

    @Override
    public String getKey() {
        return hashedDefaultFolderKey;
    }
}
