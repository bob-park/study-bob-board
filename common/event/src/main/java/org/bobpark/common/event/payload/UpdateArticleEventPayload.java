package org.bobpark.common.event.payload;

import java.time.LocalDateTime;

import lombok.Builder;

import org.bobpark.common.event.EventPayload;

@Builder
public record UpdateArticleEventPayload(Long articleId,
                                        String title,
                                        String content,
                                        Long boardId,
                                        Long writerId,
                                        LocalDateTime createdAt,
                                        LocalDateTime modifiedAt,
                                        Long boardArticleCount)
    implements EventPayload {
}
