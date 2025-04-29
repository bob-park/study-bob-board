package org.bobpark.common.event;

import java.time.LocalDateTime;

import lombok.extern.slf4j.Slf4j;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import org.bobpark.common.event.payload.CreateArticleEventPayload;

@Slf4j
class EventTest {

    @Test
    void serde() {

        // given
        CreateArticleEventPayload payload =
            CreateArticleEventPayload.builder()
                .articleId(1L)
                .title("title")
                .content("content")
                .boardId(1L)
                .writerId(1L)
                .createdAt(LocalDateTime.now())
                .modifiedAt(LocalDateTime.now())
                .boardArticleCount(23L)
                .build();

        Event<CreateArticleEventPayload> event =
            Event.<CreateArticleEventPayload>builder()
                .eventId(1234L)
                .type(EventType.ARTICLE_CREATED)
                .payload(payload)
                .build();

        // when
        String json = event.toJson();
        log.info("json = {}", json);

        Event<EventPayload> result = Event.fromJson(json);

        // then
        Assertions.assertThat(result.getEventId()).isEqualTo(event.getEventId());
        Assertions.assertThat(result.getType()).isEqualTo(event.getType());
        Assertions.assertThat(result.getPayload()).isInstanceOf(payload.getClass());

        CreateArticleEventPayload resultPayload = (CreateArticleEventPayload)result.getPayload();

        Assertions.assertThat(resultPayload.articleId()).isEqualTo(payload.articleId());
        Assertions.assertThat(resultPayload.title()).isEqualTo(payload.title());
        Assertions.assertThat(resultPayload.createdAt()).isEqualTo(payload.createdAt());

    }

}