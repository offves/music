package com.offves.music.contract.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AlbumDto implements Serializable {

    private static final long serialVersionUID = -8209490122236326478L;

    private Long id;

    private String name;

    private String picUrl;

}
