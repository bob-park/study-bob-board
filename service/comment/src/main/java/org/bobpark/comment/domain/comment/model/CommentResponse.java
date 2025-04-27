package org.bobpark.comment.domain.comment.model;

import java.time.LocalDateTime;

import lombok.Builder;

import org.bobpark.comment.domain.comment.entity.Comment;

@Builder
public record CommentResponse(Long commentId,
                              Long articleId,
                              String content,
                              Long parentCommentId,
                              Long writerId,
                              LocalDateTime createdAt) {

    public static CommentResponse from(Comment comment) {
        return CommentResponse.builder()
            .commentId(comment.getCommentId())
            .articleId(comment.getArticleId())
            .content(comment.getContent())
            .parentCommentId(comment.getParentCommentId())
            .writerId(comment.getWriterId())
            .createdAt(comment.getCreatedAt())
            .build();
    }
}
