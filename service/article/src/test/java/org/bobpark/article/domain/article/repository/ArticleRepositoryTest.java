package org.bobpark.article.domain.article.repository;

import java.util.List;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import org.junit.jupiter.api.Test;

import org.bobpark.article.domain.article.entity.Article;

@Slf4j
@SpringBootTest
@ActiveProfiles("dev")
class ArticleRepositoryTest {

    @Autowired
    ArticleRepository articleRepository;

    @Test
    void findAll() {

        List<Article> articles = articleRepository.findAll(1L, 1499970L, 30L);

        log.info("articles.size = {}", articles.size());

        for (Article article : articles) {
            log.info("article = {}", article);
        }

    }

    @Test
    void count() {
        long count = articleRepository.count(1L, 10000L);
        log.info("count = {}", count);
    }

    @Test
    void findAllInfiniteScroll() {
        List<Article> articles = articleRepository.findAllInfiniteScroll(1L, 30L);

        for (Article article : articles) {
            log.info("getArticleId = {}", article.getArticleId());
        }

        Long lastArticleId = articles.getLast().getArticleId();

        List<Article> articles2 = articleRepository.findAllInfiniteScroll(1L, lastArticleId, 30L);

        for (Article article : articles2) {
            log.info("getArticleId = {}", article.getArticleId());
        }

    }
}