package org.bobpark.hot_article.domain.hotarticle.model;

import java.time.LocalDateTime;

import lombok.Builder;

import org.bobpark.hot_article.domain.article.model.ArticleResponse;

@Builder
public record HotArticleResponse(Long articleId,
                                 String title,
                                 LocalDateTime createdAt) {

    public static HotArticleResponse from(ArticleResponse article) {
        return HotArticleResponse.builder()
            .articleId(article.articleId())
            .title(article.title())
            .createdAt(article.createdAt())
            .build();
    }
}
