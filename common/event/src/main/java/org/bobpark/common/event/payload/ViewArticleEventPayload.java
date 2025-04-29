package org.bobpark.common.event.payload;

import lombok.Builder;

import org.bobpark.common.event.EventPayload;

@Builder
public record ViewArticleEventPayload(Long articleId,
                                      Long articleViewCount)
    implements EventPayload {
}
