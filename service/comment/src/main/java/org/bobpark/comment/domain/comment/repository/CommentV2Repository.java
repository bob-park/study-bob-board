package org.bobpark.comment.domain.comment.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import org.bobpark.comment.domain.comment.entity.CommentV2;

public interface CommentV2Repository extends JpaRepository<CommentV2, Long> {

    @Query(
        value = """
            select c from CommentV2 c where c.commentPath = :path
            """)
    Optional<CommentV2> findByPath(@Param("path") String path);

    @Query(nativeQuery = true,
    value = """
        select path
        from comment_v2
        where article_id = :articleId
          and path > :pathPrefix
          and path like :pathPrefix%
        order by path desc
        limit 1   
        """)
    Optional<String> findDescendantTopPath(@Param("articleId") Long articleId, @Param("pathPrefix") String pathPrefix);

}
