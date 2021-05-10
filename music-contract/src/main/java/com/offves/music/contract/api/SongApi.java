package com.offves.music.contract.api;

import com.offves.music.common.dto.Response;
import com.offves.music.contract.dto.SongDto;

import java.util.List;

public interface SongApi {

    Response<Boolean> saveSong(SongDto song);

    Response<Boolean> saveSong(List<SongDto> songs);

    Response<List<SongDto>> getSongs(Integer pageNo, Integer pageSize);

    Response<SongDto> getSongDetail(Long songId);

    Response<List<Long>> fetchSongIdsByPage(Long lastId, Integer limit);

}
