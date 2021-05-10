package com.offves.music.dto;

import com.alibaba.fastjson.annotation.JSONField;
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
public class SongDetailDto {

    private Long id;

    private String name;

    @JSONField(name = "al")
    private Album album;

    @JSONField(name = "ar")
    private List<Singer> singers;

    private Long publishTime;

    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Data
    public static class Album {

        private Long id;

        private String name;

        private String picUrl;

    }

    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Data
    public static class Singer {

        private Long id;

        private String name;

    }

}
