package org.bobpark.common.event.payload;

import java.time.LocalDateTime;

import lombok.Builder;

import org.bobpark.common.event.EventPayload;

@Builder
public record LikeArticleEventPayload(Long archiveLikeId,
                                      Long articleId,
                                      Long userId,
                                      LocalDateTime createdAt,
                                      Long articleLikeCount)
    implements EventPayload {
}
