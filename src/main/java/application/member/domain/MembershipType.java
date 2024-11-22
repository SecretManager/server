package application.member.domain;

import lombok.Getter;

@Getter
public enum MembershipType {
    FREE(0, 5_000_000_000L, false, 10),
    BASIC(2_000, 10_000_000_000L, true, 20),
    ADVANCED(5_000, 30_000_000_000L, true, 30),
    PRO(9_000, 50_000_000_000L, true, 100),
    PREMIUM(15_000, 100_000_000_000L, true, 100),
    ELITE(25_000, 200_000_000_000L, true, 100),
    ULTRA(40_000, 500_000_000_000L, true, 100),
    ;

    private final int pricePerMonth;
    private final long savedFileLimitByte;
    private final boolean isAdBlock;
    private final int maxDownloadCountPerFileForMonth;

    MembershipType(
            int pricePerMonth,
            long savedFileLimitByte,
            boolean isAdBlock,
            int maxDownloadCountPerFileForMonth
    ) {
        this.pricePerMonth = pricePerMonth;
        this.savedFileLimitByte = savedFileLimitByte;
        this.isAdBlock = isAdBlock;
        this.maxDownloadCountPerFileForMonth = maxDownloadCountPerFileForMonth;
    }
}
