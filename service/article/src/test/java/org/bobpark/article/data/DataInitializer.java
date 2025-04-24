package org.bobpark.article.data;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionTemplate;

import org.junit.jupiter.api.Test;

import kuke.board.common.snowflake.Snowflake;

import org.bobpark.article.domain.article.entity.Article;
import org.bobpark.article.domain.article.repository.ArticleRepository;

@Slf4j
@SpringBootTest
@ActiveProfiles("dev")
public class DataInitializer {

    @Autowired
    ArticleRepository articleRepository;

    @Autowired
    TransactionTemplate transactionTemplate;

    Snowflake snowflake = new Snowflake();
    CountDownLatch latch = new CountDownLatch(EXECUTE_COUNT);

    static final int BULK_INSERT_SIZE = 2;
    static final int EXECUTE_COUNT = 6_000;

    @Test
    void initialize() throws InterruptedException {
        ExecutorService executorService = Executors.newFixedThreadPool(10);

        for (int i = 0; i < EXECUTE_COUNT; i++) {
            executorService.submit(() -> {
                insert();
                latch.countDown();
                log.info("latch.getCount() = {}", latch.getCount());
            });
        }

        latch.await();
        executorService.shutdown();
    }

    void insert() {
        transactionTemplate.executeWithoutResult(status -> {
            for (int i = 0; i < BULK_INSERT_SIZE; i++) {
                Article createdArticle =
                    Article.builder()
                        .articleId(snowflake.nextId())
                        .title("title %s".formatted(i))
                        .content("content %s".formatted(i))
                        .writerId(1L)
                        .boardId(1L)
                        .build();

                articleRepository.save(createdArticle);
            }
        });

    }
}
