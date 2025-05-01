package org.bobpark.like.service;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import org.redisson.api.RAtomicLong;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;

import com.malgn.common.exception.ServiceRuntimeException;

import org.bobpark.like.entity.ArticleViewCount;
import org.bobpark.like.repository.ArticleViewCountRepository;

@Slf4j
@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class ViewService {

    private static final Duration LOCK_TIMEOUT = Duration.ofSeconds(10);
    private static final int BATCH_INSERT_SIZE = 10;
    private static final String ARTICLE_COUNT_KEY_FORMAT = "article:%d:view:count";
    private static final String ARTICLE_COUNT_USER_LOCK_KEY_FORMAT = "article:%d:view:user:%d";

    private final ArticleViewCountRepository articleViewCountRepository;

    private final RedissonClient redissonClient;

    public long read(Long articleId) {
        return readViewCount(articleId);
    }

    @Transactional
    public long increase(Long articleId, Long userId) {

        // lock 확인
        RLock lock = redissonClient.getLock(String.format(ARTICLE_COUNT_USER_LOCK_KEY_FORMAT, articleId, userId));

        if (lock.isLocked()) {
            return readViewCount(articleId);
        }

        try {
            lock.tryLock(LOCK_TIMEOUT.toSeconds(), LOCK_TIMEOUT.toSeconds(), TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            log.warn("interrupted - {}", e.getMessage());
            throw new ServiceRuntimeException(e);
        }

        long viewCount = increaseViewCount(articleId);

        if (viewCount % BATCH_INSERT_SIZE == 0) {
            ArticleViewCount articleViewCount =
                articleViewCountRepository.findById(articleId)
                    .orElseGet(() -> {
                        ArticleViewCount createdViewCount =
                            ArticleViewCount.builder()
                                .articleId(articleId)
                                .build();

                        createdViewCount = articleViewCountRepository.save(createdViewCount);

                        return createdViewCount;
                    });

            articleViewCount.updateViewCount(viewCount);

            log.debug("backed up view count. ({})", articleViewCount);
        }

        return viewCount;
    }

    private long increaseViewCount(long articleId) {

        RAtomicLong count = redissonClient.getAtomicLong(String.format(ARTICLE_COUNT_KEY_FORMAT, articleId));

        long oldCount = count.getAndIncrement();

        return oldCount + 1;
    }

    private long readViewCount(long articleId) {

        RAtomicLong count = redissonClient.getAtomicLong(String.format(ARTICLE_COUNT_KEY_FORMAT, articleId));

        return count.get();
    }
}
