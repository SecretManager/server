package application.encrypt.application.command;

import jakarta.annotation.Nullable;

public record FolderKeyQuery(
        Long memberId,
        boolean useDefaultFolderKey,
        @Nullable String folderKey,
        @Nullable String hint
) {
}
