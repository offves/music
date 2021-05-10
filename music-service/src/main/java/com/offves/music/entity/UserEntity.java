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
@Table(name = "user", schema = "netease_cloud_music")
public class UserEntity {

    @Id
    private Long userId;

    private String nickname;

    private String remarkName;

    private Integer gender;

    private Timestamp birthday;

    private Integer province;

    private Integer city;

    private Integer level;

    private Integer userType;

    private Integer vipType;

    private String signature;

    private String description;

    private String detailDescription;

    private Boolean defaultAvatar;

    private String avatarUrl;

    private Long avatarImgId;

    private String backgroundUrl;

    private Long backgroundImgId;

    private Long listenSongs;

    private String expertTags;

    private Long artistId;

    private String artistName;

    private Integer accountStatus;

    private Boolean blacklist;

    private Integer djStatus;

    private Boolean followed;

    private Integer followeds;

    private Integer follows;

    private Boolean mutual;

    private Integer authority;

    private Boolean notFind;

    private Timestamp createTime;

    private Timestamp updateTime;

}
