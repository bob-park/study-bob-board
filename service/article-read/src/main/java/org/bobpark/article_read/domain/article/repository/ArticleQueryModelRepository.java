package org.bobpark.article_read.domain.article.repository;

import static java.util.function.Function.*;

import java.time.Duration;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

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

    public Map<Long, ArticleQueryModel> readAll(List<Long> articleIds) {

        List<String> keyList = articleIds.stream().map(this::generateKey).toList();

        return redisTemplate.opsForValue().multiGet(keyList)
            .stream()
            .filter(Objects::nonNull)
            .map(json -> DataSerializer.deserialize(json, ArticleQueryModel.class))
            .collect(Collectors.toMap(ArticleQueryModel::articleId, identity()));
    }

    private String generateKey(long articleId) {
        return KEY_FORMAT.formatted(articleId);
    }

}
