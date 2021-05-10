package com.offves.music.stream;

import com.google.common.collect.Lists;
import com.offves.music.common.Constant;
import com.offves.music.common.dto.Response;
import com.offves.music.common.util.BeanCopyUtil;
import com.offves.music.contract.api.AlbumApi;
import com.offves.music.contract.api.SingerApi;
import com.offves.music.contract.api.SongApi;
import com.offves.music.contract.dto.AlbumDto;
import com.offves.music.contract.dto.SingerDto;
import com.offves.music.contract.dto.SongDto;
import com.offves.music.dto.SongDetailDto;
import com.offves.music.redis.SingerBloomFilter;
import com.offves.music.redis.SongBloomFilter;
import com.offves.music.spider.SongSpider;
import com.offves.music.stream.event.SongEvent;
import com.offves.music.stream.messaging.SongProcessor;
import com.offves.music.util.LocalDateTimeUtil;
import com.offves.stream.EventStream;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.messaging.Message;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.util.StopWatch;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@EnableBinding(SongProcessor.class)
public class SongEventStream implements EventStream<SongEvent> {

    @Resource
    private SongProcessor songProcessor;

    @Resource
    private SongSpider songSpider;

    @DubboReference
    private SongApi songApi;

    @DubboReference
    private AlbumApi albumApi;

    @DubboReference
    private SingerApi singerApi;

    @Resource
    private SongBloomFilter songBloomFilter;

    @Resource
    private SingerBloomFilter singerBloomFilter;

    @Resource
    @Qualifier(Constant.SONG_TASK_EXECUTOR_NAME)
    private ThreadPoolTaskExecutor songTaskExecutor;

    @Override
    public boolean out(SongEvent event) {
        if (CollectionUtils.isEmpty(event.getSongIds())) {
            return false;
        }
        Message<SongEvent> message = buildMessage(event);
        boolean result = songProcessor.output().send(message);
        log.info("song-topic send. songIds: {}, result: {}", event.getSongIds(), result);
        return result;
    }

    @Override
    @StreamListener(SongProcessor.INPUT)
    public void sink(@Payload SongEvent event) {
        try {
            log.info("song-topic sink. songIds: {}", event.getSongIds());
            StopWatch stopWatch = new StopWatch();

            stopWatch.start("bloomFilter");
            List<Long> songIds = event.getSongIds().parallelStream().filter(songId -> !songBloomFilter.exists(songId)).collect(Collectors.toList());
            stopWatch.stop();

            Set<AlbumDto> albums = new HashSet<>();
            Set<SingerDto> singers = new HashSet<>();

            stopWatch.start("songDetail");
            List<SongDetailDto> songDetailDtoList = songSpider.songDetail(songIds);
            stopWatch.stop();

            stopWatch.start("convent");

            List<SongDto> songs = songDetailDtoList.parallelStream().map(songDetail -> {
                SongDto song = BeanCopyUtil.copy(songDetail, SongDto.class);
                SongDetailDto.Album album = songDetail.getAlbum();
                if (Objects.nonNull(album)) {
                    song.setAlbumId(album.getId());
                    albums.add(BeanCopyUtil.copy(album, AlbumDto.class));
                }

                List<SongDetailDto.Singer> singerList = songDetail.getSingers();
                if (CollectionUtils.isNotEmpty(singerList)) {
                    song.setSinger(singerList.stream()
                            .map(singer -> String.valueOf(singer.getId()))
                            .collect(Collectors.joining(",")));
                    List<SingerDto> singerDtoList = singerList.stream()
                            .filter(singer -> !singerBloomFilter.exists(singer.getId()))
                            .map(singer -> {
                                SingerDto singerDto = BeanCopyUtil.copy(singer, SingerDto.class);
                                singerDto.setCraw(false);
                                return singerDto;
                            }).collect(Collectors.toList());
                    singers.addAll(singerDtoList);
                }

                Long songId = songDetail.getId();
                song.setLyric(songSpider.lyric(songId));
                // song.setDownloadUrl(songUrls.stream().filter(o -> o.getKey().equals(songId)).findFirst().orElse(Pair.of(songId, StringUtils.EMPTY)).getValue());
                song.setPublishTime(LocalDateTimeUtil.getTimestamp(songDetail.getPublishTime()));
                song.setPullComment(0);
                return song;
            }).collect(Collectors.toList());
            stopWatch.stop();

            stopWatch.start("saveData");
            List<List<SongDto>> partition = Lists.partition(songs, 10);
            for (List<SongDto> songList : partition) {
                songTaskExecutor.execute(() -> {
                    Response<Boolean> songResponse = songApi.saveSong(songList);
                    if (songResponse.isSuccess()) {
                        songBloomFilter.add(songIds);
                    }
                });
            }

            List<List<SingerDto>> singerPartition = Lists.partition(new ArrayList<>(singers), 10);
            for (List<SingerDto> singerDtoList : singerPartition) {
                songTaskExecutor.execute(() -> {
                    Response<Boolean> singerResponse = singerApi.saveSinger(singerDtoList);
                    if (singerResponse.isSuccess()) {
                        songBloomFilter.add(singers.stream().map(SingerDto::getId).collect(Collectors.toList()));
                    }
                });
            }

            List<List<AlbumDto>> albumPartition = Lists.partition(new ArrayList<>(albums), 10);
            for (List<AlbumDto> albumDtoList : albumPartition) {
                songTaskExecutor.execute(() -> albumApi.saveAlbum(albumDtoList));
            }
            stopWatch.stop();

            log.info("totalTime: {} s, {}", stopWatch.getTotalTimeSeconds(), stopWatch.prettyPrint());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }

}
