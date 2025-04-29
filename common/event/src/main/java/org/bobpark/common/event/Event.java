package org.bobpark.common.event;

import static com.google.common.base.Preconditions.*;
import static org.apache.commons.lang3.ObjectUtils.*;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import org.bobpark.common.dataserializer.DataSerializer;

@ToString
@Getter
public class Event<T extends EventPayload> {

    private final Long eventId;
    private final EventType type;
    private final T payload;

    @Builder
    private Event(Long eventId, EventType type, T payload) {

        checkArgument(isNotEmpty(eventId), "eventId must be provided.");
        checkArgument(isNotEmpty(type), "type must be provided.");

        this.eventId = eventId;
        this.type = type;
        this.payload = payload;
    }

    public String toJson() {
        return DataSerializer.serialize(this);
    }

    public static Event<EventPayload> fromJson(String json) {
        EventRaw eventRaw = DataSerializer.deserialize(json, EventRaw.class);

        if (eventRaw == null) {
            return null;
        }
        EventType eventType = EventType.from(eventRaw.getType());

        if (eventType == null) {
            return null;
        }

        return Event.<EventPayload>builder()
            .eventId(eventRaw.getEventId())
            .type(eventType)
            .payload(DataSerializer.deserialize(eventRaw.getPayload(), eventType.getPayloadClass()))
            .build();
    }

    @Getter
    private static class EventRaw {
        private Long eventId;
        private String type;
        private Object payload;
    }
}
