package com.offves.music.contract.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.sql.Timestamp;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SongDto implements Serializable {

    private static final long serialVersionUID = -7383145398214228280L;

    private Long id;

    private String name;

    private String lyric;

    private String singer;

    private Long albumId;

    private String downloadUrl;

    private Timestamp publishTime;

    private Integer pullComment;

    private Timestamp updateTime;

}
