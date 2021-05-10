package com.offves.music.repository;

import com.offves.music.entity.AlbumEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentRepliedRepository extends JpaRepository<AlbumEntity, Long> {

}
