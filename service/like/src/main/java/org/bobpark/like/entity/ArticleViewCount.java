package org.bobpark.like.entity;

import static com.google.common.base.Preconditions.*;
import static org.apache.commons.lang3.ObjectUtils.*;

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
@Table(name = "articles_views_counts")
public class ArticleViewCount {

    @Id
    private Long articleId;

    private Long viewCount;

    @Builder
    private ArticleViewCount(Long articleId, Long viewCount) {

        checkArgument(articleId != null, "articleId must be provided.");

        this.articleId = articleId;
        this.viewCount = defaultIfNull(viewCount, 0L);
    }

    public void updateViewCount(long viewCount) {
        this.viewCount = viewCount;
    }
}
