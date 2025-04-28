package org.bobpark.comment.domain.comment.model;

import java.time.LocalDateTime;

import lombok.Builder;

import org.bobpark.comment.domain.comment.entity.Comment;
import org.bobpark.comment.domain.comment.entity.CommentV2;

@Builder
public record CommentResponse(Long commentId,
                              Long articleId,
                              String content,
                              Long parentCommentId,
                              String path,
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

    public static CommentResponse from(CommentV2 comment) {
        return CommentResponse.builder()
            .commentId(comment.getCommentId())
            .articleId(comment.getArticleId())
            .content(comment.getContent())
            .path(comment.getCommentPath().getPath())
            .writerId(comment.getWriterId())
            .createdAt(comment.getCreatedAt())
            .build();
    }
}
