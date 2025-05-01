package org.bobpark.article_read.domain.article.service;

import java.time.Duration;
import java.util.List;
import java.util.Optional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.malgn.common.exception.NotFoundException;

import org.bobpark.article_read.domain.article.event.handler.EventHandler;
import org.bobpark.article_read.domain.article.feign.ArticleFeignClient;
import org.bobpark.article_read.domain.article.feign.ArticleViewFeignClient;
import org.bobpark.article_read.domain.article.model.ArticleReadResponse;
import org.bobpark.article_read.domain.article.model.ArticleResponse;
import org.bobpark.article_read.domain.article.model.query.ArticleQueryModel;
import org.bobpark.article_read.domain.article.repository.ArticleQueryModelRepository;
import org.bobpark.common.event.Event;
import org.bobpark.common.event.EventPayload;

@Slf4j
@RequiredArgsConstructor
@Service
public class ArticleReadService {

    private final ArticleFeignClient articleClient;
    private final ArticleViewFeignClient articleViewClient;

    private final ArticleViewConsumer viewConsumer;

    private final ArticleQueryModelRepository repository;

    private final List<EventHandler> handlers;

    public void handleEvent(Event<EventPayload> event) {
        for (EventHandler handler : handlers) {
            if (handler.supports(event)) {
                handler.handle(event);
            }
        }
    }

    public ArticleReadResponse read(long articleId) {
        ArticleQueryModel model =
            repository.read(articleId)
                .or(() -> fetch(articleId)) // 없을 경우 command service 에서 가져옴
                .orElseThrow(() -> new NotFoundException("id: " + articleId));

        long articleView = viewConsumer.getArticleViewCount(articleId);

        return ArticleReadResponse.from(model, articleView);
    }

    private Optional<ArticleQueryModel> fetch(long articleId) {

        ArticleResponse article = articleClient.getArticle(articleId);

        ArticleQueryModel model = ArticleQueryModel.create(article, 0L);

        if (model != null) {
            repository.create(model, Duration.ofDays(1));
            log.debug("fetched article. ({})", model);
        }

        return Optional.ofNullable(model);
    }

}
