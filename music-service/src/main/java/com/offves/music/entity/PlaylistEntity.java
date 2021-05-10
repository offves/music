package com.offves.music.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.sql.Timestamp;

@Data
@Entity
@EqualsAndHashCode
@Table(name = "playlist", schema = "netease_cloud_music")
public class PlaylistEntity {

    @Id
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
