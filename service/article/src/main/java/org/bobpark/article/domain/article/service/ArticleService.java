package org.bobpark.article.domain.article.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import org.bobpark.common.snowflake.Snowflake;

import com.malgn.common.exception.NotFoundException;
import com.malgn.common.model.Id;

import org.bobpark.article.domain.article.entity.Article;
import org.bobpark.article.domain.article.model.ArticleResponse;
import org.bobpark.article.domain.article.model.CreateArticleRequest;
import org.bobpark.article.domain.article.model.UpdateArticleRequest;
import org.bobpark.article.domain.article.repository.ArticleRepository;

@Slf4j
@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class ArticleService {

    private final Snowflake snowflake = new Snowflake();
    private final ArticleRepository articleRepository;

    @Transactional
    public ArticleResponse create(CreateArticleRequest createRequest) {
        Article createdArticle =
            Article.builder()
                .articleId(snowflake.nextId())
                .title(createRequest.title())
                .content(createRequest.content())
                .boardId(createRequest.boardId())
                .writerId(createRequest.writerId())
                .build();

        createdArticle = articleRepository.save(createdArticle);

        log.debug("created article: {}", createdArticle);

        return ArticleResponse.builder()
            .articleId(createdArticle.getArticleId())
            .title(createdArticle.getTitle())
            .content(createdArticle.getContent())
            .boardId(createdArticle.getBoardId())
            .writerId(createdArticle.getWriterId())
            .createdAt(createdArticle.getCreatedAt())
            .modifiedAt(createdArticle.getModifiedAt())
            .build();
    }

    public ArticleResponse read(Id<Article, Long> articleId) {
        Article article =
            articleRepository.findById(articleId.getValue())
                .orElseThrow(() -> new NotFoundException(articleId));

        return ArticleResponse.builder()
            .articleId(article.getArticleId())
            .title(article.getTitle())
            .content(article.getContent())
            .boardId(article.getBoardId())
            .writerId(article.getWriterId())
            .createdAt(article.getCreatedAt())
            .modifiedAt(article.getModifiedAt())
            .build();
    }

    @Transactional
    public ArticleResponse update(Id<Article, Long> articleId, UpdateArticleRequest updateRequest) {
        Article article =
            articleRepository.findById(articleId.getValue())
                .orElseThrow(() -> new NotFoundException(articleId));

        article.update(updateRequest.title(), updateRequest.content());

        log.debug("updated article: {}", article);

        return ArticleResponse.builder()
            .articleId(article.getArticleId())
            .title(article.getTitle())
            .content(article.getContent())
            .boardId(article.getBoardId())
            .writerId(article.getWriterId())
            .createdAt(article.getCreatedAt())
            .modifiedAt(article.getModifiedAt())
            .build();
    }

    @Transactional
    public void delete(Id<Article, Long> articleId) {
        articleRepository.deleteById(articleId.getValue());
    }

}
