package application.file.domain.key;

import jakarta.annotation.Nullable;
import lombok.Getter;

@Getter
public class FolderKey implements Key {

    @Nullable
    private String key;

    public FolderKey(@Nullable String key) {
        this.key = key;
    }

    public static FolderKey fromPlainKey(String key) {
        if (key == null) {
            return new FolderKey(null);
        }
        return new FolderKey(key);
    }

    @Override
    public String getKey() {
        return key;
    }
}
