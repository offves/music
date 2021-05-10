package com.offves.music.api;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.offves.music.dto.*;
import com.offves.music.spider.Downloader;
import org.apache.commons.lang3.tuple.Pair;
import org.junit.jupiter.api.Test;

import java.util.List;

class ApiTest {

    @Test
    public void personalized() {
        List<PersonalizedDto> personalized = Api.personalized(100);
        System.out.println(personalized.size());
    }

    @Test
    public void playlistDetail() {
        PlaylistDetailDto playlistDetail = Api.playlistDetail(3092900085L);
        System.out.println(JSON.toJSON(playlistDetail));
    }

    @Test
    public void songDetail() {
        List<SongDetailDto> songDetails = Api.songDetail(Lists.newArrayList(513363419L));
        System.out.println(JSON.toJSONString(songDetails));
    }

    @Test
    public void lyric() {
        String lyric = Api.lyric(60086L);
        System.out.println(lyric);
    }

    @Test
    public void songUrl() {
        List<Pair<Long, String>> pairs = Api.songUrl(Lists.newArrayList(513363419L));
        System.out.println(JSON.toJSONString(pairs));
    }

    @Test
    public void songComment() {
        Pair<Integer, List<CommentDto>> pair = Api.songComment(60102L, 24640, 0L);
        System.out.println(JSON.toJSONString(pair));
    }

    @Test
    public void userProfile() {
        UserDto user = Api.userProfile(1290955538L);
        System.out.println(JSON.toJSONString(user));
    }

    @Test
    public void getSingerSongs() {
        Pair<Boolean, List<SongDetailDto>> singerSongs = Api.getSingerSongs(6452L, 100, 0);
        System.out.println(JSON.toJSONString(singerSongs));
    }

    @Test
    public void download() {
        Downloader.download("/Users/offves/Music", 4921724835L);
    }

}