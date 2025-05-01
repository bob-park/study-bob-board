package org.bobpark.common.outboxmessagerelay.domain.outbox.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import org.bobpark.common.outboxmessagerelay.domain.outbox.entity.OutBox;
import org.bobpark.common.outboxmessagerelay.domain.outbox.repository.query.OutboxQueryRepository;

public interface OutboxRepository extends JpaRepository<OutBox, Long>, OutboxQueryRepository {

}
