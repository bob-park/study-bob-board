package org.bobpark.common.outboxmessagerelay.configure;

import java.util.Map;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;

import com.google.common.collect.Maps;

@EnableAsync
@Configuration
@ComponentScan("org.bobpark.common.outboxmessagerelay")
@EnableScheduling
public class AppConfiguration {

    @Value("${spring.kafka.bootstrap-servers}")
    private String bootstrapServers;

    @Bean
    public KafkaTemplate<String, String> messageRelayKafkaTemplate() {
        Map<String, Object> configMap = Maps.newHashMap();
        configMap.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        configMap.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        configMap.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        configMap.put(ProducerConfig.ACKS_CONFIG, "all");

        return new KafkaTemplate<>(new DefaultKafkaProducerFactory<>(configMap));
    }

    @Bean
    public Executor messageRelayEventExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();

        executor.setCorePoolSize(20);
        executor.setMaxPoolSize(50);
        executor.setQueueCapacity(100);
        executor.setThreadNamePrefix("mr-pub-event-");

        return executor;

    }

    @Bean
    public Executor messageRelayPublishPendingExecutor() {
        // single thread 로만 미전송 이벤트 전송
        return Executors.newSingleThreadExecutor();
    }

}
