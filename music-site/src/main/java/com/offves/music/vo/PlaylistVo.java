package com.offves.music.vo;

import lombok.Data;

import java.sql.Timestamp;

@Data
public class PlaylistVo {

    private long id;

    private String name;

    private Long userId;

    private Integer playCount;

    private Integer shareCount;

    private Integer commentCount;

    private Long creator;

    private String tags;

    private String coverImgUrl;

    private Long coverImgId;

    private String backgroundCoverUrl;

    private Long backgroundCoverId;

    private String titleImageUrl;

    private Long titleImage;

    private String englishTitle;

    private Byte status;

    private String description;

    private Byte highQuality;

    private Byte newImported;

    private Byte specialType;

    private Timestamp createTime;

    private Timestamp updateTime;

    private Timestamp dateUpdateTime;

}
