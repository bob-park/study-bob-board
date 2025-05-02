package org.bobpark.article_read.domain.article.event.handler;

import java.time.Duration;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Component;

import org.bobpark.article_read.domain.article.model.query.ArticleQueryModel;
import org.bobpark.article_read.domain.article.repository.ArticleIdListRepository;
import org.bobpark.article_read.domain.article.repository.ArticleQueryModelRepository;
import org.bobpark.article_read.domain.article.repository.BoardArticleCountRepository;
import org.bobpark.common.event.Event;
import org.bobpark.common.event.EventType;
import org.bobpark.common.event.payload.CreateArticleEventPayload;

@Slf4j
@RequiredArgsConstructor
@Component
public class CreateArticleReadEventHandler implements EventHandler<CreateArticleEventPayload> {

    private final ArticleIdListRepository idListRepository;
    private final BoardArticleCountRepository countRepository;
    private final ArticleQueryModelRepository repository;

    @Override
    public void handle(Event<CreateArticleEventPayload> event) {

        CreateArticleEventPayload payload = event.getPayload();

        repository.create(ArticleQueryModel.create(payload), Duration.ofDays(1));
        idListRepository.add(payload.boardId(), payload.articleId(), 1_000);
        countRepository.createOrUpdate(payload.boardId(), payload.boardArticleCount());
    }

    @Override
    public boolean supports(Event<CreateArticleEventPayload> event) {
        return event.getType() == EventType.ARTICLE_CREATED;
    }
}
