package org.bobpark.comment.domain.comment.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import org.bobpark.comment.domain.comment.entity.Comment;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    @Query(nativeQuery = true,
        value = """
            select count(*)
            from (select comment_id
                  from comments
                  where article_id = :articleId
                    and parent_comment_id = :parentCommentId
                  limit :limit)
            """
    )
    Long countBy(
        @Param("articleId") Long articleId,
        @Param("parentCommentId") Long parentCommentId,
        @Param("limit") Long limit);

}
