package org.bobpark.hot_article.domain.hotarticle.repository;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.data.redis.connection.StringRedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Repository;

@Slf4j
@RequiredArgsConstructor
@Repository
public class HotArticleListRepository {

    private final StringRedisTemplate redisTemplate;

    // data key
    // hot-article::list::{yyyyMMdd}
    private static final String KEY_FORMAT = "hot-article::list::%s";
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyyMMdd");

    public void add(Long articleId, LocalDateTime time, Long score, Long limit, Duration ttl) {
        redisTemplate.executePipelined((RedisCallback<?>)redisConn -> {

            StringRedisConnection conn = (StringRedisConnection)redisConn;

            String key = generateKey(time);

            conn.zAdd(key, score, String.valueOf(articleId));
            conn.zRemRange(key, 0, -1 - limit); // 최상위 10개만 남김
            conn.expire(key, ttl.toSeconds());

            return null;
        });
    }

    public List<Long> readAll(String dateStr) {
        return redisTemplate.opsForZSet()
            .reverseRangeWithScores(generateKey(dateStr), 0, -1).stream()
            .peek(tuple ->
                log.info("[HotArticleListRepository.readAll] articleId={}, score={}", tuple.getValue(), tuple.getScore()))
            .map(ZSetOperations.TypedTuple::getValue)
            .map(Long::parseLong)
            .toList();
    }

    public void remove(Long articleId, LocalDateTime time) {
        redisTemplate.opsForZSet().remove(generateKey(time), articleId);
    }

    private String generateKey(LocalDateTime time) {
        return generateKey(DATE_TIME_FORMATTER.format(time));
    }

    private String generateKey(String date) {
        return KEY_FORMAT.formatted(date);
    }

}
