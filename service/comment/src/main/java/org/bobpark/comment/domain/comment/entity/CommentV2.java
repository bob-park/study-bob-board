package org.bobpark.comment.domain.comment.entity;

import static org.apache.commons.lang3.ObjectUtils.*;

import java.time.LocalDateTime;

import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import com.malgn.common.entity.annotation.SnowflakeIdGenerateValue;

@ToString
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "comments_v2")
public class CommentV2 {

    @Id
    @SnowflakeIdGenerateValue
    private Long commentId;

    private String content;
    private Long articleId; // shard key
    private Long writerId;
    private Boolean deleted;
    private LocalDateTime createdAt;

    @Embedded
    private CommentPath commentPath;

    @Builder
    private CommentV2(Long commentId, String content, Long articleId, Long writerId, Boolean deleted,
        LocalDateTime createdAt, CommentPath commentPath) {
        this.commentId = commentId;
        this.content = content;
        this.articleId = articleId;
        this.writerId = writerId;
        this.deleted = defaultIfNull(deleted, false);
        this.createdAt = LocalDateTime.now();
        this.commentPath = commentPath;
    }

    public boolean isRoot() {
        return getCommentPath().isRoot();
    }

    public void delete() {
        this.deleted = true;
    }
}
