package com.offves.music.contract.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.sql.Timestamp;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SingerDto implements Serializable {

    private static final long serialVersionUID = 3583462169695774528L;

    private Long id;

    private String name;

    private Boolean craw;

    private Timestamp updateTime;

}