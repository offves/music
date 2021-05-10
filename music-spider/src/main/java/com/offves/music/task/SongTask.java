package com.offves.music.task;

import com.offves.music.common.dto.Response;
import com.offves.music.contract.api.SingerApi;
import com.offves.music.stream.SingerEventStream;
import com.offves.music.stream.event.SingerEvent;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;

@Component
public class SongTask {

    @DubboReference
    private SingerApi singerApi;

    @Resource
    private SingerEventStream singerEventStream;

    public void singerSongTask(Integer limit) {
        Long lastId = 0L;
        LocalDateTime begin = LocalDateTime.of(2020, 1, 1, 0, 0, 0);
        LocalDateTime end = LocalDateTime.of(2021, 5, 1, 0, 0, 0);

        while (true) {
            Response<List<Long>> response = singerApi.fetchUnSpiderSingerIdsByPage(begin, end, lastId, limit);
            List<Long> result = response.getResult();
            if (response.isFail() || CollectionUtils.isEmpty(result)) {
                break;
            }

            result.forEach(singerId -> singerEventStream.out(new SingerEvent(singerId)));

            lastId = result.get(result.size() - 1);
        }
    }

}
