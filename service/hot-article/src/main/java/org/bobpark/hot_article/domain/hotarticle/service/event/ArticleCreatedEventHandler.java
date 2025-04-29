package org.bobpark.hot_article.domain.hotarticle.service.event;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Component;

import org.bobpark.common.event.Event;
import org.bobpark.common.event.EventType;
import org.bobpark.common.event.payload.CreateArticleEventPayload;
import org.bobpark.hot_article.domain.hotarticle.repository.ArticleCreatedTimeRepository;
import org.bobpark.hot_article.domain.hotarticle.utils.TimeCalculateUtils;

@RequiredArgsConstructor
@Component
public class ArticleCreatedEventHandler implements EventHandler<CreateArticleEventPayload> {

    private final ArticleCreatedTimeRepository articleCreatedTimeRepository;

    @Override
    public void handle(Event<CreateArticleEventPayload> event) {
        CreateArticleEventPayload payload = event.getPayload();

        articleCreatedTimeRepository.createOrUpdate(
            payload.articleId(),
            payload.createdAt(),
            TimeCalculateUtils.calculateDuration());
    }

    @Override
    public boolean supports(Event<CreateArticleEventPayload> event) {
        return event.getType() == EventType.ARTICLE_CREATED;
    }

    @Override
    public Long findArticleId(Event<CreateArticleEventPayload> event) {
        return event.getPayload().articleId();
    }
}
