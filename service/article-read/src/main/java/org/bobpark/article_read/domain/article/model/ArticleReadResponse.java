package org.bobpark.article_read.domain.article.model;

import java.time.LocalDateTime;

import lombok.Builder;

import org.bobpark.article_read.domain.article.model.query.ArticleQueryModel;

@Builder
public record ArticleReadResponse(Long articleId,
                                  String title,
                                  String content,
                                  Long writerId,
                                  LocalDateTime createdAt,
                                  LocalDateTime modifiedAt,
                                  Long articleCommentCount,
                                  Long articleViewCount) {

    public static ArticleReadResponse from(ArticleQueryModel model, long viewCount) {
        return ArticleReadResponse.builder()
            .articleId(model.articleId())
            .title(model.title())
            .content(model.content())
            .writerId(model.writerId())
            .createdAt(model.createdAt())
            .modifiedAt(model.modifiedAt())
            .articleCommentCount(model.articleCommentCount())
            .articleViewCount(viewCount)
            .build();
    }
}
