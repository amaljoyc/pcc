package com.amaljoyc.pcc.util;

import java.time.Instant;

/**
 * Created by achemparathy on 04.12.17.
 */
public final class PccUtil {

    public static final long SIXTY_SECONDS = 60l;

    private PccUtil() {
    }

    public static boolean isOlderThanSixtySeconds(Long uploadTime) {
        long sixtySecAgo = getEpoch() - SIXTY_SECONDS;
        return uploadTime < sixtySecAgo;
    }

    public static long timeToReachSixtySecondsOldFromNow(Long uploadTime) {
        long sixtySecAgo = getEpoch() - SIXTY_SECONDS;
        return uploadTime - sixtySecAgo;
    }

    public static long getEpoch() {
        return Instant.now().getEpochSecond();
    }

}
