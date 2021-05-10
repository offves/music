package com.offves.music.repository;

import com.offves.music.entity.SongEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SongRepository extends JpaRepository<SongEntity, Long> {

    @Query(value = "select id from song where id > :lastId order by id limit :limit", nativeQuery = true)
    List<Long> fetchSongIdsByPage(@Param("lastId") Long lastId, @Param("limit") Integer limit);

}
