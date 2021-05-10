package com.offves.music.contract.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PlaylistSongDto implements Serializable {

    private static final long serialVersionUID = 4926233044985398318L;

    private Long id;

    private Long playlistId;

    private Long songId;

}
