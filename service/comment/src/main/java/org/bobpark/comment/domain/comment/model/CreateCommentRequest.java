package org.bobpark.comment.domain.comment.model;

import lombok.Builder;

@Builder
public record CreateCommentRequest(Long articleId,
                                   String content,
                                   Long parentCommentId,
                                   Long writerId) {
}
