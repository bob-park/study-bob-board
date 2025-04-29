package org.bobpark.common.outboxmessagerelay.domain.outbox.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import com.malgn.common.entity.annotation.SnowflakeIdGenerateValue;

import org.bobpark.common.event.EventType;

@ToString
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "outbox")
public class OutBox {

    @Id
    @SnowflakeIdGenerateValue
    private Long id;

    @Enumerated(EnumType.STRING)
    private EventType type;

    private String payload;

    private Long shardKey;
    private LocalDateTime createdAt;

    @Builder
    private OutBox(Long id, EventType type, String payload, Long shardKey, LocalDateTime createdAt) {
        this.id = id;
        this.type = type;
        this.payload = payload;
        this.shardKey = shardKey;
        this.createdAt = LocalDateTime.now();
    }
}


