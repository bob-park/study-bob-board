package org.bobpark.article.domain.article.model;

import java.util.List;

import lombok.Builder;

@Builder
public record ArticlePageResponse(List<ArticleResponse> articles,
                                  Long articleCount) {

}
