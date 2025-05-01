package org.bobpark.article_read.domain.article.repository;

import java.time.Duration;
import java.util.Optional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Repository;

import org.bobpark.article_read.domain.article.model.query.ArticleQueryModel;
import org.bobpark.common.dataserializer.DataSerializer;

@Slf4j
@RequiredArgsConstructor
@Repository
public class ArticleQueryModelRepository {

    // key
    private static final String KEY_FORMAT = "article-read::article::%s";

    private final StringRedisTemplate redisTemplate;

    public void create(ArticleQueryModel model, Duration ttl) {
        redisTemplate.opsForValue()
            .set(generateKey(model.articleId()), DataSerializer.serialize(model));
    }

    public void update(ArticleQueryModel model) {
        redisTemplate.opsForValue()
            .setIfPresent(generateKey(model.articleId()), DataSerializer.serialize(model));
    }

    public Optional<ArticleQueryModel> read(Long articleId) {
        return Optional.ofNullable(
                redisTemplate.opsForValue().get(generateKey(articleId)))
            .map(item -> DataSerializer.deserialize(item, ArticleQueryModel.class));
    }

    private String generateKey(long articleId) {
        return KEY_FORMAT.formatted(articleId);
    }

}
