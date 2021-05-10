package com.offves.music.vo;

import lombok.Data;

import java.sql.Timestamp;

@Data
public class SongVo {

    private long id;

    private String name;

    private String lyric;

    private String singer;

    private Long albumId;

    private String downloadUrl;

    private Timestamp publishTime;

    private Integer pullComment;

    private Timestamp updateTime;

}
