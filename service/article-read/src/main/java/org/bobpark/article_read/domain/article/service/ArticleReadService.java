package org.bobpark.article_read.domain.article.service;

import java.time.Duration;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;

import com.malgn.common.exception.NotFoundException;

import org.bobpark.article_read.domain.article.event.handler.EventHandler;
import org.bobpark.article_read.domain.article.feign.ArticleFeignClient;
import org.bobpark.article_read.domain.article.feign.ArticleViewFeignClient;
import org.bobpark.article_read.domain.article.model.ArticleReadPageResponse;
import org.bobpark.article_read.domain.article.model.ArticleReadResponse;
import org.bobpark.article_read.domain.article.feign.model.ArticleResponse;
import org.bobpark.article_read.domain.article.model.query.ArticleQueryModel;
import org.bobpark.article_read.domain.article.repository.ArticleIdListRepository;
import org.bobpark.article_read.domain.article.repository.ArticleQueryModelRepository;
import org.bobpark.article_read.domain.article.repository.BoardArticleCountRepository;
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
    private final ArticleIdListRepository idListRepository;
    private final BoardArticleCountRepository countRepository;

    private final List<EventHandler> handlers;
    private final BoardArticleCountRepository boardArticleCountRepository;

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

    public ArticleReadPageResponse readAll(Long boardId, Long page, Long pageSize) {
        return ArticleReadPageResponse.of(
            readAll(
                readAllArticleIds(boardId, page, pageSize)),
            count(boardId));
    }

    public List<ArticleReadResponse> readAll(List<Long> ids) {
        Map<Long, ArticleQueryModel> maps = repository.readAll(ids);

        return ids.stream()
            .map(
                id ->
                    maps.containsKey(id) ?
                        maps.get(id) : fetch(id).orElse(null))
            .filter(Objects::nonNull)
            .map(
                model ->
                    ArticleReadResponse.from(model, viewConsumer.getArticleViewCount(model.articleId())))
            .toList();
    }

    private List<Long> readAllArticleIds(Long boardId, Long page, Long pageSize) {

        List<Long> ids = idListRepository.readAll(boardId, (page - 1) * pageSize, pageSize);

        if (pageSize == ids.size()) {
            log.debug("return redis data.");
            return ids;
        }

        log.debug("return origin data.");

        return articleClient.getArticles(boardId, page, pageSize)
            .articles()
            .stream()
            .map(ArticleResponse::articleId)
            .toList();

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

    private long count(Long boardId) {
        long count = boardArticleCountRepository.read(boardId);

        if (count < 0) {
            count = articleClient.getArticleCount(boardId);

            log.debug("fetched origin data...");
        }

        return count;
    }

}
