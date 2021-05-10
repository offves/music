package com.offves.music.redis;

import com.offves.music.common.dto.Response;
import com.offves.music.contract.api.SongApi;
import io.rebloom.client.ClusterClient;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.dubbo.config.annotation.DubboReference;
import org.redisson.api.RBucket;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;

import javax.annotation.Resource;
import java.util.List;
import java.util.Objects;

@Slf4j
@Component
public class SongBloomFilter {

    @DubboReference
    private SongApi songApi;

    @Resource
    private RedissonClient redisson;

    @Resource
    private ClusterClient clusterClient;

    public void init() {
        RBucket<Long> songLastIdBucket = redisson.getBucket("SongBloomFilterLastId");
        Long lastId = Objects.isNull(songLastIdBucket.get()) ? 0L : songLastIdBucket.get();
        while (true) {
            StopWatch sw = new StopWatch(String.valueOf(lastId));
            sw.start("fetchSongIdsByPage");
            Response<List<Long>> response = songApi.fetchSongIdsByPage(lastId, 2000);
            List<Long> ids = response.getResult();
            sw.stop();

            if (response.isFail() || CollectionUtils.isEmpty(ids)) {
                break;
            }

            sw.start("addSongBloomFilter");
            add(ids);
            sw.stop();

            log.info("totalTime: {}, {}", sw.getTotalTimeSeconds(), sw.prettyPrint());
            lastId = ids.get(ids.size() - 1);
            songLastIdBucket.set(lastId);
        }
    }

    public void add(List<Long> ids) {
        String[] values = ids.stream().map(String::valueOf).toArray(String[]::new);
        clusterClient.addMulti(getBloomFilterName(), values);
    }

    public void add(Long id) {
        clusterClient.addMulti(getBloomFilterName(), String.valueOf(id));
    }

    public boolean exists(Long id) {
        return clusterClient.exists(getBloomFilterName(), String.valueOf(id));
    }

    private String getBloomFilterName() {
        return getClass().getSimpleName();
    }

}
