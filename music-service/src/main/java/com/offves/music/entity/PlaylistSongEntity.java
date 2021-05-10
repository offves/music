package com.offves.music.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.sql.Timestamp;

@Data
@Entity
@EqualsAndHashCode
@Table(name = "playlist_song", schema = "netease_cloud_music")
public class PlaylistSongEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long playlistId;

    private Long songId;

    private Timestamp updateTime;

}
