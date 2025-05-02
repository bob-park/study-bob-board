package org.bobpark.article_read.domain.article.repository;

import lombok.RequiredArgsConstructor;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
public class BoardArticleCountRepository {

    // key format
    // article-read::board-article-count::board::{boardId}
    private static final String KEY_FORMAT = "article-read::board-article-count::board::%s";

    private final StringRedisTemplate redisTemplate;

    public void createOrUpdate(long boardId, long count) {
        redisTemplate.opsForValue().set(generateKey(boardId), String.valueOf(count));
    }

    public long read(Long boardId) {

        String result = redisTemplate.opsForValue().get(generateKey(boardId));

        if (result == null) {
            return -1;
        }

        return Long.parseLong(result);
    }

    private String generateKey(long boardId) {
        return KEY_FORMAT.formatted(boardId);
    }
}
