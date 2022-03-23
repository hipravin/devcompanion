package com.hipravin.playgroundspring;

import org.junit.jupiter.api.Test;

import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;

public class DateFormatterIso8601IT {
    @Test
    void testFormat() {
        OffsetDateTime now = OffsetDateTime.now();
        String formatterIso = DateTimeFormatter.ISO_OFFSET_DATE_TIME.format(now);
        System.out.println(formatterIso);
    }
}
