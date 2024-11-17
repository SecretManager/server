package application.member.domain;

import application.common.value.Byte;
import application.common.value.GigaByte;
import application.common.value.MegaByte;
import lombok.Getter;

@Getter
public enum Membership {
    FREE(0, new MegaByte(300), false, 10),
    BASIC(2_000, new GigaByte(10), true, 20),
    ADVANCED(5_000, new GigaByte(30), true, 30),
    PRO(9_000, new GigaByte(50), true, 100),
    PREMIUM(15_000, new GigaByte(100), true, 100),
    ELITE(25_000, new GigaByte(200), true, 100),
    ULTRA(40_000, new GigaByte(500), true, 100),
    ;

    private final int pricePerMonth;
    private final Byte savedFileLimit;
    private final boolean isAdBlock;
    private final int maxDownLoadCount;

    Membership(int pricePerMonth, Byte savedFileLimit, boolean isAdBlock, int maxDownLoadCount) {
        this.pricePerMonth = pricePerMonth;
        this.savedFileLimit = savedFileLimit;
        this.isAdBlock = isAdBlock;
        this.maxDownLoadCount = maxDownLoadCount;
    }
}
