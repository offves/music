package com.offves.music.rest;

import com.offves.music.common.dto.Response;
import com.offves.music.common.util.BeanCopyUtil;
import com.offves.music.contract.api.PlaylistApi;
import com.offves.music.contract.dto.PlaylistDto;
import com.offves.music.vo.PlaylistVo;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/playlist")
public class PlaylistController {

    @DubboReference
    private PlaylistApi playlistApi;

    @GetMapping
    public Response<List<PlaylistVo>> getAllPlaylist() {
        Response<List<PlaylistDto>> response = playlistApi.getAllPlaylist();

        if (!response.isSuccess()) {
            return Response.error();
        }

        List<PlaylistVo> playlistVos = BeanCopyUtil.copyList(response.getResult(), PlaylistVo.class);
        return Response.success(playlistVos);
    }

    @GetMapping("{playlistId}")
    public Response<PlaylistVo> getPlaylistDetail(@PathVariable Long playlistId) {
        Response<PlaylistDto> response = playlistApi.getPlaylistDetail(playlistId);

        if (!response.isSuccess()) {
            return Response.error();
        }

        PlaylistVo playlistVo = BeanCopyUtil.copy(response.getResult(), PlaylistVo.class);
        return Response.success(playlistVo);
    }

}
