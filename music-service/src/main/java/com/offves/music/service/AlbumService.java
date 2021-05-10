package com.offves.music.service;

import com.offves.music.common.dto.Response;
import com.offves.music.common.enums.ResponseEnum;
import com.offves.music.common.util.BeanCopyUtil;
import com.offves.music.contract.api.AlbumApi;
import com.offves.music.contract.dto.AlbumDto;
import com.offves.music.entity.AlbumEntity;
import com.offves.music.repository.AlbumRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

@Slf4j
@DubboService
public class AlbumService implements AlbumApi {

    @Resource
    private AlbumRepository albumRepository;

    @Override
    @Transactional
    public Response<Boolean> saveAlbum(List<AlbumDto> albums) {
        if (CollectionUtils.isEmpty(albums)) {
            return Response.error(ResponseEnum.PARAM_IS_EMPTY);
        }
        List<AlbumEntity> entities = BeanCopyUtil.copyList(albums, AlbumEntity.class);
        for (AlbumEntity entity : entities) {
            try {
                albumRepository.save(entity);
            } catch (Exception e) {
                log.error(e.getMessage(), e);
            }

        }
        return Response.success(true);
    }

}
