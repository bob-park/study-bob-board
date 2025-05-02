package org.bobpark.article_read.domain.article.feign.model;

import java.time.LocalDateTime;

public record ArticleResponse(Long articleId,
                              String title,
                              String content,
                              Long boardId,
                              Long writerId,
                              LocalDateTime createdAt,
                              LocalDateTime modifiedAt) {
}
