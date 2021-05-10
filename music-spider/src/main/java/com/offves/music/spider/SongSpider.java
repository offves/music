package com.offves.music.spider;

import com.offves.music.api.Api;
import com.offves.music.common.Constant;
import com.offves.music.dto.SongDetailDto;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.assertj.core.util.Lists;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

@Slf4j
@Service
public class SongSpider {

    @Resource
    @Qualifier(Constant.SONG_TASK_EXECUTOR_NAME)
    private ThreadPoolTaskExecutor songTaskExecutor;

    public List<SongDetailDto> songDetail(List<Long> songIds) {
        return Api.songDetail(songIds);
    }

    public String lyric(Long songId) {
        return Api.lyric(songId);
    }

    public List<Long> singerSongs(Long singerId) {
        int limit = 100;
        List<Long> songIds = Lists.newArrayList();

        Integer total = Api.getSingerSongsTotal(singerId, 1, 0);
        List<Future<List<SongDetailDto>>> futures = new ArrayList<>();
        for (int j = 0; j < Math.ceil((double) total / 100); j++) {

            int finalJ = j;
            Future<List<SongDetailDto>> future = songTaskExecutor.submit(() -> Api.getSingerSongs2(singerId, limit, finalJ * limit));
            futures.add(future);
        }

        for (Future<List<SongDetailDto>> future : futures) {
            try {
                songIds.addAll(future.get().stream().map(SongDetailDto::getId).collect(Collectors.toList()));
            } catch (InterruptedException | ExecutionException e) {
                log.error(e.getMessage(), e);
            }
        }

        return songIds;
    }

    private void singerSongs(Long singerId, Integer offset, List<Long> songIds) {
        Integer limit = 100;
        Pair<Boolean, List<SongDetailDto>> singerSongs = Api.getSingerSongs(singerId, limit, offset * limit);

        List<SongDetailDto> songs = singerSongs.getRight();
        if (CollectionUtils.isNotEmpty(songs)) {
            songIds.addAll(songs.stream().map(SongDetailDto::getId).collect(Collectors.toList()));
        }

        if (singerSongs.getLeft()) {
            singerSongs(singerId, offset + 1, songIds);
        }
    }

}
