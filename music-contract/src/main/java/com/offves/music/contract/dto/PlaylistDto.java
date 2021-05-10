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
public class PlaylistDto implements Serializable {

    private static final long serialVersionUID = -8432669270381624878L;

    private Long id;

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

    private Integer status;

    private String description;

    private Boolean highQuality;

    private Boolean newImported;

    private Integer specialType;

    private Timestamp createTime;

    private Timestamp updateTime;

    private Timestamp dateUpdateTime;

}
