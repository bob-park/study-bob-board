package org.bobpark.like.service;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClient.ResponseSpec;

import org.junit.jupiter.api.Test;

@Slf4j
class ViewServiceTest {

    RestClient restClient = RestClient.create("http://localhost:9004/api");

    @Test
    void increase() throws Exception {

        ExecutorService executorService = Executors.newFixedThreadPool(100);
        CountDownLatch latch = new CountDownLatch(10000);

        for (int i = 0; i < 10000; i++) {
            executorService.submit(() -> {
                restClient.post()
                    .uri("/v1/articles-views/{articleId}/users/{userId}", 4L, 1L)
                    .retrieve();

                latch.countDown();
            });

        }

        latch.await();

        Long count =
            restClient.get()
                .uri("/v1/articles-views/{articleId}", 4)
                .retrieve()
                .body(Long.class);

        log.info("count = {}", count);

    }
}