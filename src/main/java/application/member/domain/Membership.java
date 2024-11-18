package application.member.domain;

import application.common.Default;
import application.common.exception.ForbiddenException;
import lombok.Getter;

@Getter
public class Membership {

    private long currentSavedFileBytes;
    private MembershipType membershipType;

    public Membership(MembershipType membershipType) {
        this.currentSavedFileBytes = 0;
        this.membershipType = membershipType;
    }

    @Default
    public Membership(long currentSavedFileBytes, MembershipType membershipType) {
        this.currentSavedFileBytes = currentSavedFileBytes;
        this.membershipType = membershipType;
    }

    public void uploadFile(long fileSize) {
        int savedFileLimitMegaByte = membershipType.getSavedFileLimitByte();
        if (savedFileLimitMegaByte < currentSavedFileBytes + fileSize) {
            throw new ForbiddenException("더이상 파일을 저장할 수 없습니다.");
        }
        currentSavedFileBytes += fileSize;
    }

    public void downloadFile(int currentDownloadCountPerMonth) {
        int maxDownloadCountPerFileForMonth = membershipType.getMaxDownloadCountPerFileForMonth();
        if (currentDownloadCountPerMonth >= maxDownloadCountPerFileForMonth) {
            throw new ForbiddenException("더이상 파일을 다운로드 할 수 없습니다.");
        }
    }
}
