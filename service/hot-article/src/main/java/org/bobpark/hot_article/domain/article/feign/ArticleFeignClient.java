package org.bobpark.hot_article.domain.article.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import org.bobpark.hot_article.domain.article.model.ArticleResponse;

@FeignClient(name = "bob-article", contextId = "board-article-service")
public interface ArticleFeignClient {

    @GetMapping(path = "api/v1/articles/{articleId:\\d+}")
    ArticleResponse getArticle(@PathVariable Long articleId);
}
