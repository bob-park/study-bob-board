package org.bobpark.hot_article.domain.hotarticle.utils;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;

public interface TimeCalculateUtils {

    static Duration calculateDuration() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime midnight = now.plusDays(1).with(LocalTime.MIDNIGHT); // 금일 자정

        return Duration.between(now, midnight);
    }
}
