package org.bobpark.article_read.domain.article.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "board-like-service", contextId = "board-like-service")
public interface ArticleViewFeignClient {

    @GetMapping(path = "api/v1/articles-views/{articleId:\\d+}")
    Long getArticleViewCount(@PathVariable Long articleId);
}
