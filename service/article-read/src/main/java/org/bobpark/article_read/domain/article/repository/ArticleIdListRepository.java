package org.bobpark.article_read.domain.article.repository;

import java.util.List;

import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Range;
import org.springframework.data.redis.connection.Limit;
import org.springframework.data.redis.connection.StringRedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
public class ArticleIdListRepository {

    // key format
    // article-read::board::{boardId}::article-list
    private static final String KEY_FORMAT = "article-read::board::%s::article-list";

    private final StringRedisTemplate redisTemplate;

    public void add(long boardId, long articleId, long limit) {
        redisTemplate.executePipelined((RedisCallback<?>)action -> {
            StringRedisConnection conn = (StringRedisConnection)action;

            String key = generateKey(boardId);

            // score 의 value 값으로 정렬되기 때문에, 같은 문자열로 만들어 최신순서를 유지
            conn.zAdd(key.getBytes(), 0, toPaddedString(articleId).getBytes());

            return null;
        });
    }

    public void delete(long boardId, long articleId) {
        redisTemplate.opsForZSet().remove(generateKey(boardId), toPaddedString(articleId));
    }

    public List<Long> readAll(long boardId, Long offset, long limit) {
        return redisTemplate.opsForZSet()
            .reverseRange(generateKey(boardId), offset, offset + limit - 1)
            .stream()
            .map(Long::parseLong)
            .toList();
    }

    public List<Long> readAllInfiniteScroll(long boardId, Long lastArticleId, long limit) {
        return redisTemplate.opsForZSet()
            .reverseRangeByLex(
                generateKey(boardId),
                lastArticleId == null ?
                    Range.unbounded() :
                    // Range.Bound.exclusive(..) 이후의 limit 만큼 가져옴
                    Range.leftUnbounded(Range.Bound.exclusive(toPaddedString(lastArticleId))),
                Limit.limit().count((int)limit))
            .stream()
            .map(Long::parseLong)
            .toList();
    }

    private String toPaddedString(long articleId) {
        return "%019d".formatted(articleId);
    }

    private String generateKey(long boardId) {
        return KEY_FORMAT.formatted(boardId);
    }

}
