package org.bobpark.hot_article.domain.hotarticle.controller;

import java.util.List;

import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import org.bobpark.hot_article.domain.hotarticle.model.HotArticleResponse;
import org.bobpark.hot_article.domain.hotarticle.service.HotArticleService;

@RequiredArgsConstructor
@RestController
@RequestMapping("v1/hot-articles")
public class HotArticleController {

    private final HotArticleService hotArticleService;

    @GetMapping(path = "articles/date/{dateStr}")
    public List<HotArticleResponse> readAll(@PathVariable String dateStr) {
        return hotArticleService.readAll(dateStr);
    }

}
