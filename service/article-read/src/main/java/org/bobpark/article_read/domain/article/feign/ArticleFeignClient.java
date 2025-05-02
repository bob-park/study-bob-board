package org.bobpark.article_read.domain.article.feign;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import org.bobpark.article_read.domain.article.feign.model.ArticlePageResponse;
import org.bobpark.article_read.domain.article.feign.model.ArticleResponse;

@FeignClient(name = "bob-article", contextId = "board-article-service")
public interface ArticleFeignClient {

    @GetMapping(path = "api/v1/articles/{articleId:\\d+}")
    ArticleResponse getArticle(@PathVariable Long articleId);

    @GetMapping(path = "api/v1/articles")
    ArticlePageResponse getArticles(@RequestParam("boardId") Long boardId, @RequestParam("page") Long page, @RequestParam("pageSize") Long pageSize);

    @GetMapping(path = "api/v1/articles/infinite-scroll")
    List<ArticleResponse> getArticlesInfiniteScroll(@RequestParam("boardId") Long boardId, @RequestParam("lastArticleId") Long lastArticleId, @RequestParam("pageSize") Long pageSize);

    @GetMapping(path = "api/v1/articles/boards/{boardId:\\d+}/count")
    Long getArticleCount(@PathVariable Long boardId);
}
