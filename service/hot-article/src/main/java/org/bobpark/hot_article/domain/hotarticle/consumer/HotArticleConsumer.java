package org.bobpark.hot_article.domain.hotarticle.consumer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

import org.bobpark.common.event.Event;
import org.bobpark.common.event.EventPayload;
import org.bobpark.common.event.EventType.Topic;
import org.bobpark.hot_article.domain.hotarticle.service.HotArticleService;

@Slf4j
@RequiredArgsConstructor
@Component
public class HotArticleConsumer {
    private final HotArticleService hotArticleService;

    @KafkaListener(topics = {
        Topic.BOB_BOARD_ARTICLE,
        Topic.BOB_BOARD_COMMENT,
        Topic.BOB_BOARD_LIKE,
        Topic.BOB_BOARD_VIEW})
    public void listen(String message, Acknowledgment ack) {
        log.info("receive message: {}", message);

        Event<EventPayload> event = Event.fromJson(message);

        if (event != null) {
            hotArticleService.handleEvent(event);
        }

        ack.acknowledge(); // 실행이 완료됬다고 kafka 에 전달
    }

}
