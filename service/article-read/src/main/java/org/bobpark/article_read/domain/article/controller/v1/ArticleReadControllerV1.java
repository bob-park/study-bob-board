package org.bobpark.article_read.domain.article.controller.v1;

import jakarta.ws.rs.Path;

import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import org.bobpark.article_read.domain.article.model.ArticleReadResponse;
import org.bobpark.article_read.domain.article.service.ArticleReadService;

@RequiredArgsConstructor
@RestController
@RequestMapping("v1/articles")
public class ArticleReadControllerV1 {

    private final ArticleReadService readService;

    @GetMapping(path = "{articleId:\\d+}")
    public ArticleReadResponse read(@PathVariable Long articleId) {
        return readService.read(articleId);
    }

}
