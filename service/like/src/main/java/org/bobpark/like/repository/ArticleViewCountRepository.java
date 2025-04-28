package org.bobpark.like.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import org.bobpark.like.entity.ArticleViewCount;

public interface ArticleViewCountRepository extends JpaRepository<ArticleViewCount, Long> {
}
