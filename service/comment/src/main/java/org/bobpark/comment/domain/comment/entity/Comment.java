package org.bobpark.comment.domain.comment.entity;

import static org.apache.commons.lang3.ObjectUtils.*;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@ToString
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "comments")
public class Comment {

    @Id
    private Long commentId;

    private String content;
    private Long parentCommentId;
    private Long articleId; // shard key
    private Long writerId;
    private Boolean deleted;
    private LocalDateTime createdAt;

    @Builder
    private Comment(Long commentId, String content, Long parentCommentId, Long articleId, Boolean deleted,
        LocalDateTime createdAt, Long writerId) {
        this.commentId = commentId;
        this.content = content;
        this.writerId = writerId;
        this.parentCommentId = defaultIfNull(parentCommentId, commentId);
        this.articleId = articleId;
        this.deleted = defaultIfNull(deleted, false);
        this.createdAt = LocalDateTime.now();
    }

    public boolean isRoot() {
        return getParentCommentId().longValue() == getCommentId().longValue();
    }

    public void delete() {
        this.deleted = true;
    }
}
