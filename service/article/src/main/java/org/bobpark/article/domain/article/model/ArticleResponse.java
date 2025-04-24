package org.bobpark.article.domain.article.model;

import java.time.LocalDateTime;

import lombok.Builder;

@Builder
public record ArticleResponse(Long articleId,
                              String title,
                              String content,
                              Long boardId,
                              Long writerId,
                              LocalDateTime createdAt,
                              LocalDateTime modifiedAt) {
}
