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
@Table(name = "comment", schema = "netease_cloud_music")
public class CommentEntity {

    @Id
    private Long commentId;

    private Long songId;

    private Long userId;

    private String content;

    private Integer commentLocationType;

    private Boolean liked;

    private Integer likedCount;

    private Boolean status;

    private Timestamp time;

    private Timestamp updateTime;

}
