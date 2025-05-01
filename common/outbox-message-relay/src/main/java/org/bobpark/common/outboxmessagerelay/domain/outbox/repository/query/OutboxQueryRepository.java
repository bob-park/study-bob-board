package org.bobpark.common.outboxmessagerelay.domain.outbox.repository.query;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Pageable;

import org.bobpark.common.outboxmessagerelay.domain.outbox.entity.OutBox;

public interface OutboxQueryRepository {

    List<OutBox> findAll(Long shardKey, LocalDateTime from, Pageable pageable);

}
