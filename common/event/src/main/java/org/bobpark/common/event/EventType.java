package org.bobpark.common.event;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

import org.bobpark.common.event.payload.CreateArticleEventPayload;
import org.bobpark.common.event.payload.DeleteArticleEventPayload;
import org.bobpark.common.event.payload.LikeArticleEventPayload;
import org.bobpark.common.event.payload.UnlikeArticleEventPayload;
import org.bobpark.common.event.payload.UpdateArticleEventPayload;
import org.bobpark.common.event.payload.ViewArticleEventPayload;

@Slf4j
@ToString
@Getter
@RequiredArgsConstructor
public enum EventType {

    ARTICLE_CREATED(CreateArticleEventPayload.class, Topic.BOB_BOARD_ARTICLE),
    ARTICLE_UPDATED(UpdateArticleEventPayload.class, Topic.BOB_BOARD_ARTICLE),
    ARTICLE_DELETED(DeleteArticleEventPayload.class, Topic.BOB_BOARD_ARTICLE),
    COMMENT_CREATED(CreateArticleEventPayload.class, Topic.BOB_BOARD_COMMENT),
    COMMENT_DELETED(DeleteArticleEventPayload.class, Topic.BOB_BOARD_COMMENT),
    ARTICLE_LIKED(LikeArticleEventPayload.class, Topic.BOB_BOARD_LIKE),
    ARTICLE_UNLIKED(UnlikeArticleEventPayload.class, Topic.BOB_BOARD_LIKE),
    ARTICLE_VIEWED(ViewArticleEventPayload.class, Topic.BOB_BOARD_VIEW);

    private final Class<? extends EventPayload> payloadClass;
    private final String topic;

    public static EventType from(String type) {

        try {
            return valueOf(type);
        } catch (Exception e) {
            log.error("[EventType.from] type={}", type, e);
            return null;
        }
    }

    public static class Topic {
        public static final String BOB_BOARD_ARTICLE = "bob-board-article";
        public static final String BOB_BOARD_COMMENT = "bob-board-comment";
        public static final String BOB_BOARD_LIKE = "bob-board-like";
        public static final String BOB_BOARD_VIEW = "bob-board-view";
    }

}
