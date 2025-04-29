package org.bobpark.hot_article.domain.hotarticle.repository;

import java.time.Duration;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Repository;

@Slf4j
@RequiredArgsConstructor
@Repository
public class ArticleViewCountRepository {

    private final StringRedisTemplate redisTemplate;

    // hot-article::article::{articleId}::like-count
    private static final String KEY_FORMAT = "hot-article::article::%d::view-count";

    public void createOrUpdate(Long articleId, Long viewCount, Duration ttl) {
        redisTemplate.opsForValue().set(generateKey(articleId), String.valueOf(viewCount), ttl);
    }

    public Long read(Long articleId) {
        String result = redisTemplate.opsForValue().get(generateKey(articleId));

        return result == null ? 0L : Long.parseLong(result);
    }

    private String generateKey(Long articleId) {
        return KEY_FORMAT.formatted(articleId);
    }

}
