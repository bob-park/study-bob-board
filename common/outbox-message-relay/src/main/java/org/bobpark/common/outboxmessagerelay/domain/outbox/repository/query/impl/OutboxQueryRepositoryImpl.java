package org.bobpark.common.outboxmessagerelay.domain.outbox.repository.query.impl;

import static org.bobpark.common.outboxmessagerelay.domain.outbox.entity.QOutBox.*;

import java.time.LocalDateTime;
import java.util.List;

import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Pageable;

import com.querydsl.jpa.impl.JPAQueryFactory;

import org.bobpark.common.outboxmessagerelay.domain.outbox.entity.OutBox;
import org.bobpark.common.outboxmessagerelay.domain.outbox.repository.query.OutboxQueryRepository;

@RequiredArgsConstructor
public class OutboxQueryRepositoryImpl implements OutboxQueryRepository {

    private final JPAQueryFactory query;

    @Override
    public List<OutBox> findAll(Long shardKey, LocalDateTime from, Pageable pageable) {
        return query.selectFrom(outBox)
            .where(
                outBox.shardKey.eq(shardKey),
                outBox.createdAt.before(from))
            .orderBy(outBox.createdAt.asc())
            .limit(pageable.getPageSize())
            .offset(pageable.getOffset())
            .fetch();
    }
}
