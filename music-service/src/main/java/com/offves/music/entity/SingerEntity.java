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
@Table(name = "singer", schema = "netease_cloud_music")
public class SingerEntity {

    @Id
    private Long id;

    private String name;

    private Boolean craw;

    private Timestamp updateTime;

}
