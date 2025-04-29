package org.bobpark.common.event.payload;

import java.time.LocalDateTime;

import lombok.Builder;

import org.bobpark.common.event.EventPayload;

@Builder
public record DeleteCommentEventPayload(Long commentId,
                                        String content,
                                        String path,
                                        Long articleId,
                                        Long writerId,
                                        Boolean deleted,
                                        LocalDateTime createdAt,
                                        Long articleCommentCount)
    implements EventPayload {
}
