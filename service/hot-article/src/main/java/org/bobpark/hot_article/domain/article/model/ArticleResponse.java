package org.bobpark.hot_article.domain.article.model;

import java.time.LocalDateTime;

public record ArticleResponse(Long articleId,
                              String title,
                              String content,
                              Long boardId,
                              Long writerId,
                              LocalDateTime createdAt,
                              LocalDateTime modifiedAt) {
}
