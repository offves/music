package com.offves.music.stream;

import com.offves.music.common.util.BeanCopyUtil;
import com.offves.music.contract.api.PlaylistApi;
import com.offves.music.contract.dto.PlaylistDto;
import com.offves.music.contract.dto.PlaylistSongDto;
import com.offves.music.dto.PlaylistDetailDto;
import com.offves.music.dto.TrackIdDto;
import com.offves.music.dto.UserDto;
import com.offves.music.spider.PlaylistSpider;
import com.offves.music.stream.event.PlaylistEvent;
import com.offves.music.stream.event.SongEvent;
import com.offves.music.stream.messaging.PlaylistProcessor;
import com.offves.music.util.LocalDateTimeUtil;
import com.offves.stream.EventStream;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.messaging.Message;
import org.springframework.messaging.handler.annotation.Payload;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@EnableBinding(PlaylistProcessor.class)
public class PlaylistEventStream implements EventStream<PlaylistEvent> {

    @Resource
    private PlaylistProcessor playlistProcessor;

    @Resource
    private PlaylistSpider playlistSpider;

    @Resource
    private SongEventStream songEventStream;

    @DubboReference
    private PlaylistApi playlistApi;

    @Override
    public boolean out(PlaylistEvent event) {
        if (Objects.isNull(event.getPlaylistId())) {
            return false;
        }
        Message<PlaylistEvent> message = buildMessage(event);
        boolean result = playlistProcessor.output().send(message);
        log.info("playlist-topic send. playlistId: {}, result: {}", event.getPlaylistId(), result);
        return result;
    }

    @Override
    @StreamListener(PlaylistProcessor.INPUT)
    public void sink(@Payload PlaylistEvent event) {
        try {
            log.info("playlist-topic sink. playlistId: {}", event.getPlaylistId());
            Long playlistId = event.getPlaylistId();

            PlaylistDetailDto playlistDetail = playlistSpider.playlistDetail(playlistId);
            if (Objects.isNull(playlistDetail)) {
                return;
            }

            savePlaylist(playlistDetail);

            List<Long> songIds = playlistDetail.getTrackIds().stream().map(TrackIdDto::getId).collect(Collectors.toList());

            List<PlaylistSongDto> playlistSongDtoList = songIds.stream().map(songId -> new PlaylistSongDto(null, playlistId, songId)).collect(Collectors.toList());

            playlistApi.savePlaylistSong(playlistSongDtoList);

            songEventStream.out(new SongEvent(songIds));
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }

    private void savePlaylist(PlaylistDetailDto playlistDetail) {
        PlaylistDto playlistDto = BeanCopyUtil.copy(playlistDetail, PlaylistDto.class);
        playlistDto.setCreator(Optional.ofNullable(playlistDetail.getCreator()).orElse(new UserDto()).getUserId());
        playlistDto.setCreateTime(LocalDateTimeUtil.getTimestamp(playlistDetail.getCreateTime()));
        playlistDto.setUpdateTime(LocalDateTimeUtil.getTimestamp(playlistDetail.getUpdateTime()));
        playlistDto.setTags(String.join(",", Optional.ofNullable(playlistDetail.getTags()).orElse(Collections.emptyList())));

        playlistApi.savePlaylist(playlistDto);
    }

}
