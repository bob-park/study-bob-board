package org.bobpark.hot_article.domain.hotarticle.service;

import java.util.List;
import java.util.Objects;

import jakarta.annotation.PostConstruct;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;

import org.bobpark.common.event.Event;
import org.bobpark.common.event.EventPayload;
import org.bobpark.common.event.EventType;
import org.bobpark.hot_article.domain.article.feign.ArticleFeignClient;
import org.bobpark.hot_article.domain.hotarticle.model.HotArticleResponse;
import org.bobpark.hot_article.domain.hotarticle.repository.HotArticleListRepository;
import org.bobpark.hot_article.domain.hotarticle.service.event.EventHandler;

@Slf4j
@RequiredArgsConstructor
@Service
public class HotArticleService {

    private final ArticleFeignClient articleClient;
    private final List<EventHandler> eventHandlers;
    private final HotArticleScoreUpdater scoreUpdater;

    private final HotArticleListRepository hotArticleListRepository;

    @PostConstruct
    public void init() {
        log.info("init hot article service");
    }


    public void handleEvent(Event<EventPayload> event) {
        EventHandler<EventPayload> eventHandler = findEventHandler(event);

        if (eventHandler == null) {
            log.warn("no event handler found for event {}", event);
            return;
        }

        if (isArticleCreatedOrDeleted(event)) {
            eventHandler.handle(event);
        } else {
            scoreUpdater.update(event, eventHandler);
        }
    }

    public List<HotArticleResponse> readAll(String dateStr) {
        return hotArticleListRepository.readAll(dateStr).stream()
            .map(articleClient::getArticle)
            .filter(Objects::nonNull)
            .map(HotArticleResponse::from)
            .toList();
    }

    private  EventHandler<EventPayload> findEventHandler(Event<EventPayload> event) {
        return eventHandlers.stream()
            .filter(eventHandler -> eventHandler.supports(event))
            .findAny()
            .orElse(null);
    }

    private boolean isArticleCreatedOrDeleted(Event<EventPayload> event) {
        return EventType.ARTICLE_CREATED == event.getType() || EventType.ARTICLE_DELETED == event.getType();
    }

}
