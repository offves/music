package com.offves.music.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author offves
 */
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class PlaylistDetailDto {

    private Long id;

    private String name;

    private Long userId;

    private Integer playCount;

    private Integer shareCount;

    private Integer commentCount;

    private UserDto creator;

    private List<UserDto> subscribers;

    private List<TrackDto> tracks;

    private List<TrackIdDto> trackIds;

    private List<String> tags;

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

    private Long createTime;

    private Long updateTime;

}
