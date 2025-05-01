package org.bobpark.common.outboxmessagerelay.domain.message;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.TimeUnit;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.data.domain.Pageable;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import com.malgn.common.exception.ServiceRuntimeException;

import org.bobpark.common.outboxmessagerelay.domain.outbox.entity.OutBox;
import org.bobpark.common.outboxmessagerelay.domain.outbox.event.OutBoxEvent;
import org.bobpark.common.outboxmessagerelay.domain.outbox.repository.OutboxRepository;
import org.bobpark.common.outboxmessagerelay.domain.shard.AssignedShard;

@Slf4j
@RequiredArgsConstructor
@Component
public class MessageRelay {

    private final OutboxRepository outboxRepository;
    private final MessageRelayCoordinator coordinator;
    private final KafkaTemplate<String, String> kafkaTemplate;
    private final KafkaTemplate<String, String> messageRelayKafkaTemplate;

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @TransactionalEventListener(phase = TransactionPhase.BEFORE_COMMIT)
    public void createOutbox(OutBoxEvent event) {
        log.debug("createOutbox - {}", event);

        outboxRepository.save(event.getOutBox());
    }

    @Async("messageRelayEventExecutor")
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void publishEvent(OutBoxEvent event) {
        publishEvent(event.getOutBox());
    }

    @Scheduled(fixedDelay = 10,
        initialDelay = 5,
        timeUnit = TimeUnit.SECONDS,
        scheduler = "messageRelayPublishPendingExecutor")
    public void publishPendingEvents() {

        AssignedShard assignedShard = coordinator.assignShard();
        log.info("assigned shard size={}", assignedShard.getShards().size());

        for (Long shard : assignedShard.getShards()) {
            List<OutBox> outboxs =
                outboxRepository.findAll(
                    shard,
                    LocalDateTime.now().minusSeconds(10),
                    Pageable.ofSize(10));

            for (OutBox outbox : outboxs) {
                publishEvent(outbox);
            }
        }

    }

    private void publishEvent(OutBox outbox) {
        try {
            messageRelayKafkaTemplate.send(
                    outbox.getType().getTopic(),
                    String.valueOf(outbox.getShardKey()),
                    outbox.getPayload())
                .get(1, TimeUnit.SECONDS);
        } catch (Exception e) {
            log.error("outbox={}", outbox, e);
            throw new ServiceRuntimeException(e);
        }

        outboxRepository.delete(outbox);
    }

}
