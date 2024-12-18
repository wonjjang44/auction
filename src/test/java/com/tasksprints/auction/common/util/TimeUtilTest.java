package com.tasksprints.auction.common.util;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import java.time.Clock;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class TimeUtilTest {
    private final ZoneId zoneId = ZoneId.of("Asia/Seoul");
    private final Instant fixedInstant = Instant.parse("2024-10-22T10:00:00Z");
    @Mock
    private Clock clock;

    @BeforeEach
    void setUp() {
        when(clock.instant()).thenReturn(fixedInstant);
        when(clock.getZone()).thenReturn(zoneId);
    }

    @Test
    @DisplayName("LocalDateTime -> Date 변환 테스트")
    void localDateTimeToDate() {
        // given
        LocalDateTime localDateTime = LocalDateTime.now(clock);

        // when
        Date date = TimeUtil.localDateTimeToDate(localDateTime);

        // then
        assertEquals(localDateTime.atZone(zoneId).toInstant(), date.toInstant());
    }
}
