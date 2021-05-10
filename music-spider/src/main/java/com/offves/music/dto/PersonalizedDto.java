package com.offves.music.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 推荐歌单
 * @author offves
 */
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class PersonalizedDto {

    private Long id;

    private Integer type;

    private String name;

    private String copywriter;

    private String picUrl;

    private Boolean canDislike;

    private Long trackNumberUpdateTime;

    private Integer playCount;

    private Integer trackCount;

    private Boolean highQuality;

    private String alg;

}
