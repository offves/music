package com.offves.music.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author offves
 */
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class AlDto {

    private String picUrl;

    private String name;

}
