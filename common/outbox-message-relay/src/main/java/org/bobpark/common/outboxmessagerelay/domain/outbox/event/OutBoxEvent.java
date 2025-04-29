package org.bobpark.common.outboxmessagerelay.domain.outbox.event;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import org.bobpark.common.outboxmessagerelay.domain.outbox.entity.OutBox;

@ToString
@Getter
public class OutBoxEvent {

    private final OutBox outBox;

    @Builder
    private OutBoxEvent(OutBox outBox) {
        this.outBox = outBox;
    }
}
