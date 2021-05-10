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
public class CommentDto {

    private Long commentId;

    private UserDto user;

    private String content;

    private Integer commentLocationType;

    private Boolean liked;

    private Integer likedCount;

    private Long beRepliedCommentId;

    private Integer status;

    private Long time;

    /**
     * 回复
     */
    private List<CommentDto> beReplied;

}
