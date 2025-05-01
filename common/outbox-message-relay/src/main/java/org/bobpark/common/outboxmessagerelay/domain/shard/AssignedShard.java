package org.bobpark.common.outboxmessagerelay.domain.shard;

import java.util.List;
import java.util.stream.LongStream;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@ToString
@Getter
public class AssignedShard {

    private final List<Long> shards;

    @Builder
    private AssignedShard(String appId, List<String> appIds, Long shardCount) {
        this.shards = assign(appId, appIds, shardCount);
    }

    public static List<Long> assign(String appId, List<String> appIds, Long shardCount) {
        int appIndex = findAppIndex(appId, appIds);

        if (appIndex == -1) {
            return List.of();
        }

        long start = appIndex * shardCount / appIds.size();
        long end = (appIndex + 1) * shardCount / appIds.size() - 1;

        return LongStream.rangeClosed(start, end).boxed().toList();
    }

    private static int findAppIndex(String appId, List<String> appIds) {
        for (int i = 0; i < appIds.size(); i++) {
            if (appIds.get(i).equals(appId)) {
                return i;
            }
        }

        return -1;
    }
}
