package com.offves.music.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 用户
 * @author offves
 */
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class UserDto {

    private Long userId;

    private String nickname;

    private String remarkName;

    private Integer gender;

    private Long birthday;

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

    private Long followTime;

    private Boolean followMe;

    private Long createTime;

}
