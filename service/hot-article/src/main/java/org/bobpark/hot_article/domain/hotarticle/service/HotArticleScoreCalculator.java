package org.bobpark.hot_article.domain.hotarticle.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Component;

import org.bobpark.hot_article.domain.hotarticle.repository.ArticleCommentCountRepository;
import org.bobpark.hot_article.domain.hotarticle.repository.ArticleLikeCountRepository;
import org.bobpark.hot_article.domain.hotarticle.repository.ArticleViewCountRepository;

@Slf4j
@RequiredArgsConstructor
@Component
public class HotArticleScoreCalculator {

    private static final long ARTICLE_LIKE_COUNT_WEIGHT = 3;
    private static final long ARTICLE_COMMENT_COUNT_WEIGHT = 2;
    private static final long ARTICLE_VIEW_COUNT_WEIGHT = 1;

    private final ArticleLikeCountRepository likeCountRepository;
    private final ArticleViewCountRepository viewCountRepository;
    private final ArticleCommentCountRepository commentCountRepository;

    public long calculate(Long articleId) {
        Long likeCount = likeCountRepository.read(articleId);
        Long commentCount = commentCountRepository.read(articleId);
        Long viewCount = viewCountRepository.read(articleId);

        return (likeCount * ARTICLE_LIKE_COUNT_WEIGHT)
            + (commentCount * ARTICLE_COMMENT_COUNT_WEIGHT)
            + (viewCount * ARTICLE_VIEW_COUNT_WEIGHT);

    }
}
