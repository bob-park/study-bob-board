package org.bobpark.hot_article.domain.hotarticle.repository;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Repository;

@Slf4j
@RequiredArgsConstructor
@Repository
public class ArticleCreatedTimeRepository {

    private final StringRedisTemplate redisTemplate;

    // data key
    // hot-article::article::{articleId}::comment-count
    private static final String KEY_FORMAT = "hot-article::article::%d::created-time";

    public void createOrUpdate(Long articleId, LocalDateTime time, Duration ttl) {
        redisTemplate.opsForValue()
            .set(
                generateKey(articleId),
                String.valueOf(time.toInstant(ZoneOffset.UTC).toEpochMilli()),
                ttl);
    }

    public void delete(Long articleId) {
        redisTemplate.delete(generateKey(articleId));
    }

    public LocalDateTime read(Long articleId) {
        String result = redisTemplate.opsForValue().get(generateKey(articleId));

        if (result == null) {
            return null;
        }

        return LocalDateTime.ofInstant(Instant.ofEpochMilli(Long.parseLong(result)), ZoneOffset.UTC);
    }

    private String generateKey(Long articleId) {
        return KEY_FORMAT.formatted(articleId);
    }

}
