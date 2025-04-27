package org.bobpark.article.domain.article.utils;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

class PageLimitCalculatorTest {

    @Test
    void calculatePageLimit() {
        calculatePageLimit(1L, 30L, 10L, 301L);
    }

    void calculatePageLimit(Long page, Long pageSize, Long movablePageCount, long expected) {
        Long result = PageLimitCalculator.calculatePageLimit(page, pageSize, movablePageCount);
        Assertions.assertThat(result).isEqualTo(expected);
    }
}