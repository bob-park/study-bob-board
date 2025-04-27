package org.bobpark.article.domain.article.model;

import java.time.LocalDateTime;

import lombok.Builder;

import org.bobpark.article.domain.article.entity.Article;

@Builder
public record ArticleResponse(Long articleId,
                              String title,
                              String content,
                              Long boardId,
                              Long writerId,
                              LocalDateTime createdAt,
                              LocalDateTime modifiedAt) {

    public static ArticleResponse from(Article article) {
        return ArticleResponse.builder()
            .articleId(article.getArticleId())
            .title(article.getTitle())
            .content(article.getContent())
            .boardId(article.getBoardId())
            .writerId(article.getWriterId())
            .createdAt(article.getCreatedAt())
            .modifiedAt(article.getModifiedAt())
            .build();
    }
}
