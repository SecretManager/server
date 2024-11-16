package application.encrypt.domain.key;

import jakarta.annotation.Nullable;
import lombok.Getter;

@Getter
public class FolderKey implements Key {

    @Nullable
    private String key;
    private String hint;

    public FolderKey(@Nullable String key, @Nullable String hint) {
        this.key = key;
        this.hint = hint;
    }

    public static FolderKey ofPlainKeyForEncrypt(String key, String hint) {
        if (key == null) {
            return new FolderKey(null, null);
        }
        return new FolderKey(key, hint);
    }

    public static FolderKey fromPlainKeyForDecrypt(String key) {
        if (key == null) {
            return new FolderKey(null, null);
        }
        return new FolderKey(key, null);
    }

    @Override
    public String getKey() {
        return key;
    }
}
