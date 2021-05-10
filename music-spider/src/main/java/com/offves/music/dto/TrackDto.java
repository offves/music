package com.offves.music.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 歌曲 ID
 * @author offves
 */
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class TrackDto {

    private Long id;

    private String name;

    private AlDto al;

    private Long publishTime;

}
