package org.bobpark.comment.domain.comment.model;

import lombok.Builder;

@Builder
public record CreateCommentRequestV2(Long articleId,
                                     String parentPath,
                                     String content,
                                     Long writerId) {
}
