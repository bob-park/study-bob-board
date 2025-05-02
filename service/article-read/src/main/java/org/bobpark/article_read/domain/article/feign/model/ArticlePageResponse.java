package org.bobpark.article_read.domain.article.feign.model;

import java.util.List;

import lombok.Builder;

@Builder
public record ArticlePageResponse(List<ArticleResponse> articles,
                                  Long articleCount) {

}
