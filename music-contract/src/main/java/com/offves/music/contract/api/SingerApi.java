package com.offves.music.contract.api;

import com.offves.music.common.dto.Response;
import com.offves.music.contract.dto.SingerDto;

import java.time.LocalDateTime;
import java.util.List;

public interface SingerApi {

    Response<Boolean> saveSinger(List<SingerDto> singers);

    Response<List<Long>> fetchSingerIdsByPage(Long lastId, Integer limit);

    Response<List<Long>> fetchUnSpiderSingerIdsByPage(LocalDateTime begin, LocalDateTime end, Long lastId, Integer limit);

    Response<Boolean> updateSingerSpiderStatus(Long singerId);

}
