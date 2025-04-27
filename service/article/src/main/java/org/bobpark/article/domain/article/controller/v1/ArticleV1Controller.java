package org.bobpark.article.domain.article.controller.v1;

import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.Mapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.malgn.common.model.Id;

import org.bobpark.article.domain.article.entity.Article;
import org.bobpark.article.domain.article.model.ArticlePageResponse;
import org.bobpark.article.domain.article.model.ArticleResponse;
import org.bobpark.article.domain.article.model.CreateArticleRequest;
import org.bobpark.article.domain.article.model.UpdateArticleRequest;
import org.bobpark.article.domain.article.service.ArticleService;

@RequiredArgsConstructor
@RestController
@RequestMapping("v1/articles")
public class ArticleV1Controller {

    private final ArticleService articleService;

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(path = "")
    public ArticleResponse create(@RequestBody CreateArticleRequest createRequest) {
        return articleService.create(createRequest);
    }

    @GetMapping(path = "")
    public ArticlePageResponse readAll(Long boardId, Long page, Long pageSize) {
        return articleService.readAll(boardId, page, pageSize);
    }

    @GetMapping(path = "{articleId:\\d+}")
    public ArticleResponse read(@PathVariable Long articleId) {
        return articleService.read(Id.of(Article.class, articleId));
    }

    @PutMapping(path = "{articleId:\\d+}")
    public ArticleResponse update(@PathVariable Long articleId, @RequestBody UpdateArticleRequest updateRequest) {
        return articleService.update(Id.of(Article.class, articleId), updateRequest);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping(path = "{articleId:\\d+}")
    public ArticleResponse delete(@PathVariable Long articleId) {
        articleService.delete(Id.of(Article.class, articleId));

        return ArticleResponse.builder()
            .articleId(articleId)
            .build();
    }

}
