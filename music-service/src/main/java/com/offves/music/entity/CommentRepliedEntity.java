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
@Table(name = "comment_replied", schema = "netease_cloud_music")
public class CommentRepliedEntity {

    @Id
    private Long id;

    private Long commentId;

    private Long repliedUserId;

    private String repliedContent;

    private Timestamp updateTime;

}
