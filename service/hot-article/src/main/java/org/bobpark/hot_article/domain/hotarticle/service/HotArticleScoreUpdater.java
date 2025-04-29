package org.bobpark.hot_article.domain.hotarticle.service;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Component;

import org.bobpark.common.event.Event;
import org.bobpark.common.event.EventPayload;
import org.bobpark.hot_article.domain.hotarticle.repository.ArticleCreatedTimeRepository;
import org.bobpark.hot_article.domain.hotarticle.repository.HotArticleListRepository;
import org.bobpark.hot_article.domain.hotarticle.service.event.EventHandler;

@Slf4j
@RequiredArgsConstructor
@Component
public class HotArticleScoreUpdater {

    private static final long HOT_ARTICLE_COUNT = 10;
    private static final Duration HOT_ARTICLE_TTL = Duration.ofDays(10);

    private final HotArticleListRepository hotArticleListRepository;
    private final HotArticleScoreCalculator scoreCalculator;
    private final ArticleCreatedTimeRepository articleCreatedTimeRepository;

    public void update(Event<EventPayload> event, EventHandler<EventPayload> eventHandler) {
        Long articleId = eventHandler.findArticleId(event);

        LocalDateTime createdTime = articleCreatedTimeRepository.read(articleId);

        if (!isArticleCreatedToday(createdTime)) {
            return;
        }

        eventHandler.handle(event);

        long score = scoreCalculator.calculate(articleId);

        hotArticleListRepository.add(articleId, createdTime, score, HOT_ARTICLE_COUNT, HOT_ARTICLE_TTL);
    }

    private boolean isArticleCreatedToday(LocalDateTime createdTime) {
        return createdTime != null && createdTime.toLocalDate().isEqual(LocalDate.now());
    }
}
