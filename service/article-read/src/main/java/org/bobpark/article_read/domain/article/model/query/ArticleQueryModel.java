package org.bobpark.article_read.domain.article.model.query;

import java.time.LocalDateTime;

import lombok.Builder;

import org.bobpark.article_read.domain.article.model.ArticleResponse;
import org.bobpark.common.event.payload.CreateArticleEventPayload;

@Builder
public record ArticleQueryModel(Long articleId,
                                String title,
                                String content,
                                Long writerId,
                                LocalDateTime createdAt,
                                LocalDateTime modifiedAt,
                                Long articleCommentCount) {

    public static ArticleQueryModel create(CreateArticleEventPayload payload) {
        return ArticleQueryModel.builder()
            .articleId(payload.articleId())
            .title(payload.title())
            .content(payload.content())
            .writerId(payload.writerId())
            .createdAt(payload.createdAt())
            .modifiedAt(payload.modifiedAt())
            .articleCommentCount(0L)
            .build();
    }

    public static ArticleQueryModel create(ArticleResponse article, Long commentCount) {
        return ArticleQueryModel.builder()
            .articleId(article.articleId())
            .title(article.title())
            .content(article.content())
            .writerId(article.writerId())
            .createdAt(article.createdAt())
            .modifiedAt(article.modifiedAt())
            .articleCommentCount(commentCount)
            .build();
    }

}
