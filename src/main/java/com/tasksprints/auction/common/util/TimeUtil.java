package com.tasksprints.auction.common.util;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

public class TimeUtil {
    private static final String zoneId = "Asia/Seoul";

    public static Date localDateTimeToDate(LocalDateTime localDateTime) {
        return Date.from(localDateTime.atZone(ZoneId.of(zoneId)).toInstant());
    }
}
