package com.offves.music.contract.api;

import com.offves.music.common.dto.Response;
import com.offves.music.contract.dto.AlbumDto;

import java.util.List;

public interface AlbumApi {

    Response<Boolean> saveAlbum(List<AlbumDto> albums);

}
