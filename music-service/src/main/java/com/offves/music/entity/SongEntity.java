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
@Table(name = "song", schema = "netease_cloud_music")
public class SongEntity {

    @Id
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
