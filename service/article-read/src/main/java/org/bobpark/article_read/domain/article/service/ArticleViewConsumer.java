package org.bobpark.article_read.domain.article.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import org.bobpark.article_read.domain.article.feign.ArticleViewFeignClient;

@Slf4j
@RequiredArgsConstructor
@Component
public class ArticleViewConsumer {

    private final ArticleViewFeignClient viewClient;

    @Cacheable(key = "#articleId", value = "articleViewCount")
    public long getArticleViewCount(long articleId) {
        return viewClient.getArticleViewCount(articleId);
    }

}
