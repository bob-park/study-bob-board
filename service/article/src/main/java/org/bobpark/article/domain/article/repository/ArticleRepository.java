package org.bobpark.article.domain.article.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import org.bobpark.article.domain.article.entity.Article;

public interface ArticleRepository extends JpaRepository<Article, Long> {

    @Query(
        nativeQuery = true,
        value = """
            select articles.article_id,
                   articles.title,
                   articles.content,
                   articles.board_id,
                   articles.writer_id,
                   articles.created_at,
                   articles.modified_at
            from (select article_id
                  from articles
                  where board_id = :boardId
                  order by article_id desc
                  limit :limit offset :offset) t
                     left join articles on t.article_id = articles.article_id
            """)
    List<Article> findAll(
        @Param("boardId") Long boardId,
        @Param("offset") Long offset,
        @Param("limit") Long limit);

    @Query(
        nativeQuery = true,
        value = """
            select count(*)
            from (select article_id
                  from articles
                  where board_id = :boardId
                  limit :limit) t;
            """
    )
    Long count(@Param("boardId") Long boardId, @Param("limit") Long limit);

    @Query(
        nativeQuery = true,
        value = """
            select articles.article_id,
                   articles.title,
                   articles.content,
                   articles.board_id,
                   articles.writer_id,
                   articles.created_at,
                   articles.modified_at
            from articles
            where board_id = :boardId
            order by article_id desc
            limit :limit
            """
    )
    List<Article> findAllInfiniteScroll(@Param("boardId") Long boardId, @Param("limit") Long limit);

    @Query(
        nativeQuery = true,
        value = """
            select articles.article_id,
                   articles.title,
                   articles.content,
                   articles.board_id,
                   articles.writer_id,
                   articles.created_at,
                   articles.modified_at
            from articles
            where board_id = :boardId
                        and article_id < :lastArticleId
            order by article_id desc
            limit :limit
            """
    )
    List<Article> findAllInfiniteScroll(@Param("boardId") Long boardId, @Param("lastArticleId") Long lastArticleId,
        @Param("limit") Long limit);
}
