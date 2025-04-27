package org.bobpark.comment.data;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.support.TransactionTemplate;

import org.junit.jupiter.api.Test;

import org.bobpark.comment.domain.comment.entity.Comment;
import org.bobpark.comment.domain.comment.repository.CommentRepository;
import org.bobpark.common.snowflake.Snowflake;

@Slf4j
@SpringBootTest
@ActiveProfiles("dev")
class DataInitializer {

    @Autowired
    CommentRepository commentRepository;

    @Autowired
    TransactionTemplate transactionTemplate;

    Snowflake snowflake = new Snowflake();
    CountDownLatch latch = new CountDownLatch(EXECUTE_COUNT);

    static final int BULK_INSERT_SIZE = 2_000;
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

            Comment prev = null;

            for (int i = 0; i < BULK_INSERT_SIZE; i++) {
                Comment createdComment =
                    Comment.builder()
                        .commentId(snowflake.nextId())
                        .articleId(1L)
                        .content("content %s".formatted(i))
                        .parentCommentId(i % 2 == 0 ? null : prev.getCommentId())
                        .writerId(1L)
                        .build();

                prev = createdComment;

                createdComment = commentRepository.save(createdComment);
            }
        });

    }
}
