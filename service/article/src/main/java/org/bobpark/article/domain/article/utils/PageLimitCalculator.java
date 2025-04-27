package org.bobpark.article.domain.article.utils;

public interface PageLimitCalculator {
    static Long calculatePageLimit(Long page, Long pageSize, Long movablePageCount) {
        return (((page - 1) / movablePageCount) + 1) * pageSize * movablePageCount + 1;
    }
}
