package org.bobpark.article.domain.article.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import org.bobpark.article.domain.article.entity.Article;

public interface ArticleRepository extends JpaRepository<Article, Long> {
}
