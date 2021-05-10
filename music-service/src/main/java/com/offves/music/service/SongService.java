package com.offves.music.service;

import com.offves.music.common.dto.Response;
import com.offves.music.common.enums.ResponseEnum;
import com.offves.music.common.util.BeanCopyUtil;
import com.offves.music.contract.api.SongApi;
import com.offves.music.contract.dto.SongDto;
import com.offves.music.entity.SongEntity;
import com.offves.music.repository.SongRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;
import java.util.Objects;

@Slf4j
@DubboService
public class SongService implements SongApi {

    @Resource
    private SongRepository songRepository;

    @Override
    @Transactional
    public Response<Boolean> saveSong(SongDto song) {
        if (Objects.isNull(song)) {
            return Response.error(ResponseEnum.PARAM_IS_EMPTY);
        }
        songRepository.save(BeanCopyUtil.copy(song, SongEntity.class));
        return Response.success(true);
    }

    @Override
    @Transactional
    public Response<Boolean> saveSong(List<SongDto> songs) {
        // if (CollectionUtils.isEmpty(songs)) {
        //     return Response.error(ResponseEnum.PARAM_IS_EMPTY);
        // }
        // List<SongEntity> entities = BeanCopyUtil.copyList(songs, SongEntity.class);
        // songRepository.saveAll(entities);

        for (SongDto song : songs) {
            try {
                saveSong(song);
            } catch (Exception e) {
                log.error(e.getMessage(), e);
            }
        }
        return Response.success(true);
    }

    @Override
    @Transactional(readOnly = true)
    public Response<List<SongDto>> getSongs(Integer pageNo, Integer pageSize) {
        List<SongEntity> songEntities = songRepository.findAll();
        List<SongDto> songs = BeanCopyUtil.copyList(songEntities, SongDto.class);
        return Response.success(songs);
    }

    @Override
    @Transactional(readOnly = true)
    public Response<SongDto> getSongDetail(Long songId) {
        SongEntity songEntity = songRepository.getOne(songId);
        SongDto song = BeanCopyUtil.copy(songEntity, SongDto.class);
        return Response.success(song);
    }

    @Override
    public Response<List<Long>> fetchSongIdsByPage(Long lastSongId, Integer limit) {
        List<Long> songIds = songRepository.fetchSongIdsByPage(lastSongId, limit);
        return Response.success(songIds);
    }

}
