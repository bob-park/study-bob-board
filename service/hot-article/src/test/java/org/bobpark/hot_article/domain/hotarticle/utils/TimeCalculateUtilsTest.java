package org.bobpark.hot_article.domain.hotarticle.utils;

import static org.junit.jupiter.api.Assertions.*;

import java.time.Duration;

import lombok.extern.slf4j.Slf4j;

import org.junit.jupiter.api.Test;

@Slf4j
class TimeCalculateUtilsTest {

    @Test
    void calculateDuration() {

        Duration duration = TimeCalculateUtils.calculateDuration();

        log.debug("duration = {}", duration);

        System.out.println(duration.toMinutes());

    }
}