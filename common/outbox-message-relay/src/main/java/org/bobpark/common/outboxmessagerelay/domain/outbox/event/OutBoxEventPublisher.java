package org.bobpark.common.outboxmessagerelay.domain.outbox.event;

import lombok.RequiredArgsConstructor;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import com.malgn.common.id.showflake.Snowflake;

import org.bobpark.common.event.Event;
import org.bobpark.common.event.EventPayload;
import org.bobpark.common.event.EventType;
import org.bobpark.common.outboxmessagerelay.domain.message.MessageRelayConstants;
import org.bobpark.common.outboxmessagerelay.domain.outbox.entity.OutBox;

@RequiredArgsConstructor
@Component
public class OutBoxEventPublisher {

    private final Snowflake outboxIdSnowflake = new Snowflake();
    private final Snowflake eventIdSnowflake = new Snowflake();

    private final ApplicationEventPublisher publisher;

    public void publish(EventType type, EventPayload payload, Long shardKey) {

        OutBox createdOutbox =
            OutBox.builder()
                .type(type)
                .payload(
                    Event.builder()
                        .eventId(eventIdSnowflake.nextId())
                        .type(type)
                        .payload(payload)
                        .build()
                        .toJson())
                .shardKey(shardKey % MessageRelayConstants.SHARD_COUNT)
                .build();

        publisher.publishEvent(OutBoxEvent.builder().outBox(createdOutbox).build());
    }

}
