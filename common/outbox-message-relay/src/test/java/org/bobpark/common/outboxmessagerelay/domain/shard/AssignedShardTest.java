package org.bobpark.common.outboxmessagerelay.domain.shard;

import java.util.List;
import java.util.stream.Stream;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

class AssignedShardTest {

    @Test
    void test() {

        // given
        long shardCount = 64;
        List<String> appList = List.of("appId1", "appId2", "appId3");

        //when
        AssignedShard assignedShard1 =
            AssignedShard.builder()
                .appId(appList.get(0))
                .appIds(appList)
                .shardCount(shardCount)
                .build();
        AssignedShard assignedShard2 =
            AssignedShard.builder()
                .appId(appList.get(1))
                .appIds(appList)
                .shardCount(shardCount)
                .build();
        AssignedShard assignedShard3 =
            AssignedShard.builder()
                .appId(appList.get(2))
                .appIds(appList)
                .shardCount(shardCount)
                .build();
        AssignedShard assignedShard4 =
            AssignedShard.builder()
                .appId("invalid")
                .appIds(appList)
                .shardCount(shardCount)
                .build();

        // then
        List<Long> result =
            Stream.of(
                    assignedShard1.getShards(),
                    assignedShard2.getShards(),
                    assignedShard3.getShards(),
                    assignedShard4.getShards())
                .flatMap(List::stream)
                .toList();

        Assertions.assertThat(result).hasSize((int)shardCount);

        for (int i = 0; i < 64; i++) {
            Assertions.assertThat(result.get(i)).isEqualTo(i);
        }

        Assertions.assertThat(assignedShard4.getShards()).isEmpty();

    }

}