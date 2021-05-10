package com.offves.music.repository;

import com.offves.music.entity.SingerEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface SingerRepository extends JpaRepository<SingerEntity, Long> {

    @Query(value = "select id from singer where id > :lastId order by id limit :limit", nativeQuery = true)
    List<Long> fetchSongIdsByPage(@Param("lastId") Long lastId, @Param("limit") Integer limit);

    @Query(value = "select id from singer where id > :lastId and craw = 0 order by id limit :limit", nativeQuery = true)
    List<Long> fetchUnSpiderSingerIdsByPage(@Param("lastId") Long lastId, @Param("limit") Integer limit);

    @Modifying
    @Transactional
    @Query(value = "update singer set craw = 1 where id = ?1", nativeQuery = true)
    int updateSingerSpiderStatus(Long singerId);

}
