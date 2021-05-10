package com.offves.music.contract.api;

import com.offves.music.common.dto.Response;
import com.offves.music.contract.dto.PlaylistDto;
import com.offves.music.contract.dto.PlaylistSongDto;

import java.util.List;

public interface PlaylistApi {

    Response<Boolean> savePlaylist(PlaylistDto playlist);

    Response<Boolean> savePlaylist(List<PlaylistDto> playlists);

    Response<List<PlaylistDto>> getAllPlaylist();

    Response<PlaylistDto> getPlaylistDetail(Long playlistId);

    Response<Boolean> savePlaylistSong(List<PlaylistSongDto> playlistId);

}
