package com.offves.music.service;

import com.offves.music.common.dto.Response;
import com.offves.music.common.enums.ResponseEnum;
import com.offves.music.common.util.BeanCopyUtil;
import com.offves.music.contract.api.SingerApi;
import com.offves.music.contract.dto.SingerDto;
import com.offves.music.entity.SingerEntity;
import com.offves.music.repository.SingerRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@DubboService
public class SingerService implements SingerApi {

    @Resource
    private SingerRepository singerRepository;

    @Override
    @Transactional
    public Response<Boolean> saveSinger(List<SingerDto> singers) {
        if (CollectionUtils.isEmpty(singers)) {
            return Response.error(ResponseEnum.PARAM_IS_EMPTY);
        }
        List<SingerEntity> entities = BeanCopyUtil.copyList(singers, SingerEntity.class);
        // singerRepository.saveAll(entities);
        for (SingerEntity entity : entities) {
            try {
                singerRepository.save(entity);
            } catch (Exception e) {
                log.error(e.getMessage(), e);
            }
        }
        return Response.success(true);
    }

    @Override
    public Response<List<Long>> fetchSingerIdsByPage(Long lastId, Integer limit) {
        List<Long> singerIds = singerRepository.fetchSongIdsByPage(lastId, limit);
        return Response.success(singerIds);
    }

    @Override
    public Response<List<Long>> fetchUnSpiderSingerIdsByPage(LocalDateTime begin, LocalDateTime end, Long lastId, Integer limit) {
        List<Long> singerIds = singerRepository.fetchUnSpiderSingerIdsByPage(lastId, limit);
        return Response.success(singerIds);
    }

    @Override
    public Response<Boolean> updateSingerSpiderStatus(Long singerId) {
        int result = singerRepository.updateSingerSpiderStatus(singerId);
        return Response.success(result > 0);
    }

}
