package org.bobpark.article_read.domain.article.consumer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

import org.bobpark.article_read.domain.article.service.ArticleReadService;
import org.bobpark.common.event.Event;
import org.bobpark.common.event.EventPayload;
import org.bobpark.common.event.EventType.Topic;

@Slf4j
@RequiredArgsConstructor
@Component
public class ArticleReadEventConsumer {

    private final ArticleReadService articleReadService;

    @KafkaListener(topics = {Topic.BOB_BOARD_ARTICLE})
    public void listen(String message, Acknowledgment ack) {
        log.debug("received event. ({})", message);

        Event<EventPayload> event = Event.fromJson(message);

        if (event == null) {
            return;
        }

        articleReadService.handleEvent(event);

        ack.acknowledge();
    }
}
