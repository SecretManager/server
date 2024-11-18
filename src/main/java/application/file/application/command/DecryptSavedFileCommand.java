package application.file.application.command;

import application.member.domain.Member;

public record DecryptSavedFileCommand(
        Long id,
        Member member,
        String folderKey
) {
}
