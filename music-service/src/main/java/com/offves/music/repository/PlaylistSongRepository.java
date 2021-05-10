package com.offves.music.repository;

import com.offves.music.entity.PlaylistSongEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PlaylistSongRepository extends JpaRepository<PlaylistSongEntity, Long> {

}
