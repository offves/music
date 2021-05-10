package com.offves.music.service;

import com.offves.music.common.dto.Response;
import com.offves.music.common.enums.ResponseEnum;
import com.offves.music.common.util.BeanCopyUtil;
import com.offves.music.contract.api.PlaylistApi;
import com.offves.music.contract.dto.PlaylistDto;
import com.offves.music.contract.dto.PlaylistSongDto;
import com.offves.music.entity.PlaylistEntity;
import com.offves.music.entity.PlaylistSongEntity;
import com.offves.music.repository.PlaylistRepository;
import com.offves.music.repository.PlaylistSongRepository;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;
import java.util.Objects;

@DubboService
public class PlaylistService implements PlaylistApi {

    @Resource
    private PlaylistRepository playlistRepository;

    @Resource
    private PlaylistSongRepository playlistSongRepository;

    @Override
    @Transactional
    public Response<Boolean> savePlaylist(PlaylistDto playlist) {
        if (Objects.isNull(playlist)) {
            return Response.error(ResponseEnum.PARAM_IS_EMPTY);
        }
        PlaylistEntity playlistEntity = BeanCopyUtil.copy(playlist, PlaylistEntity.class);
        playlistRepository.save(playlistEntity);
        return Response.success(true);
    }

    @Override
    @Transactional
    public Response<Boolean> savePlaylist(List<PlaylistDto> playlists) {
        if (CollectionUtils.isEmpty(playlists)) {
            return Response.error(ResponseEnum.PARAM_IS_EMPTY);
        }
        playlistRepository.saveAll(BeanCopyUtil.copyList(playlists, PlaylistEntity.class));
        return Response.success(true);
    }

    @Override
    @Transactional(readOnly = true)
    public Response<List<PlaylistDto>> getAllPlaylist() {
        List<PlaylistEntity> playlistEntities = playlistRepository.findAll();
        List<PlaylistDto> playlists = BeanCopyUtil.copyList(playlistEntities, PlaylistDto.class);
        return Response.success(playlists);
    }

    @Override
    @Transactional(readOnly = true)
    public Response<PlaylistDto> getPlaylistDetail(Long playlistId) {
        PlaylistEntity playlistEntity = playlistRepository.getOne(playlistId);
        PlaylistDto playlist = BeanCopyUtil.copy(playlistEntity, PlaylistDto.class);
        return Response.success(playlist);
    }

    @Override
    @Transactional
    public Response<Boolean> savePlaylistSong(List<PlaylistSongDto> playlistSongs) {
        if (CollectionUtils.isEmpty(playlistSongs)) {
            return Response.error(ResponseEnum.PARAM_IS_EMPTY);
        }
        List<PlaylistSongEntity> entities = BeanCopyUtil.copyList(playlistSongs, PlaylistSongEntity.class);
        playlistSongRepository.saveAll(entities);
        return Response.success();
    }

}
