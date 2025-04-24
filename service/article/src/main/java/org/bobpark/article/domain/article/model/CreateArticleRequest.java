package org.bobpark.article.domain.article.model;

public record CreateArticleRequest(String title,
                                   String content,
                                   Long writerId,
                                   Long boardId) {
}
