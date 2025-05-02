package org.bobpark.article_read.domain.article.model;

import java.util.List;

import lombok.Builder;

@Builder
public record ArticleReadPageResponse(List<ArticleReadResponse> articles,
                                      Long articleCount) {

    public static ArticleReadPageResponse of(List<ArticleReadResponse> articles, long articleCount) {
        return ArticleReadPageResponse.builder()
            .articles(articles)
            .articleCount(articleCount)
            .build();
    }

}
