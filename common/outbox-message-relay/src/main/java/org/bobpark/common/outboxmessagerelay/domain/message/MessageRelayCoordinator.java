package org.bobpark.common.outboxmessagerelay.domain.message;

import java.time.Instant;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import jakarta.annotation.PreDestroy;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.connection.StringRedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import org.bobpark.common.outboxmessagerelay.domain.shard.AssignedShard;

@Slf4j
@RequiredArgsConstructor
@Component
public class MessageRelayCoordinator {

    private static final long PING_INTERVAL_SECONDS = 3;
    private static final long PING_FAILURE_THRESHOLD = 3;
    private static final String APP_ID = UUID.randomUUID().toString();

    private final StringRedisTemplate redisTemplate;

    @Value("${spring.application.name}")
    private String appName;

    @Scheduled(fixedDelay = PING_INTERVAL_SECONDS, timeUnit = TimeUnit.SECONDS)
    public void ping() {
        redisTemplate.executePipelined((RedisCallback<?>)action -> {

            StringRedisConnection conn = (StringRedisConnection)action;
            String key = generateKey();
            conn.zAdd(key, Instant.now().toEpochMilli(), APP_ID);

            conn.zRemRangeByScore(
                key,
                Double.NEGATIVE_INFINITY,
                Instant.now().minusSeconds(PING_INTERVAL_SECONDS * PING_FAILURE_THRESHOLD).toEpochMilli());

            return null;
        });
    }

    @PreDestroy
    public void leave() {
        redisTemplate.opsForZSet().remove(generateKey(), APP_ID);
    }

    public AssignedShard assignShard() {
        return AssignedShard.builder()
            .appId(APP_ID)
            .appIds(findAppIds())
            .shardCount(MessageRelayConstants.SHARD_COUNT)
            .build();
    }

    private String generateKey() {
        return "message-relay-coordinator::app-list::%s".formatted(appName);
    }

    private List<String> findAppIds() {
        return redisTemplate.opsForZSet().reverseRange(generateKey(), 0, -1)
            .stream()
            .sorted()
            .toList();
    }

}
