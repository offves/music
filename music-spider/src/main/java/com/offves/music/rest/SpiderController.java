package com.offves.music.rest;

import com.offves.music.common.dto.Response;
import com.offves.music.redis.SingerBloomFilter;
import com.offves.music.redis.SongBloomFilter;
import com.offves.music.spider.PlaylistSpider;
import com.offves.music.task.SongTask;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/spider")
public class SpiderController {

    @Resource
    private SongBloomFilter songBloomFilter;

    @Resource
    private SingerBloomFilter singerBloomFilter;

    @Resource
    private PlaylistSpider playlistSpider;

    @Resource
    private SongTask songTask;

    @GetMapping("/initSongBloomFilter")
    public Response<Boolean> initSongBloomFilter() {
        songBloomFilter.init();
        return Response.success();
    }

    @GetMapping("/initSingerBloomFilter")
    public Response<Boolean> initSingerBloomFilter() {
        singerBloomFilter.init();
        return Response.success();
    }

    @GetMapping("/personalized")
    public Response<Boolean> personalized(@RequestParam(defaultValue = "100") Integer limit) {
        playlistSpider.personalized(limit);
        return Response.success();
    }

    @GetMapping("/singerSongTask")
    public Response<Boolean> singerSongTask(@RequestParam(defaultValue = "2000") Integer limit) {
        CompletableFuture.runAsync(() -> songTask.singerSongTask(limit));
        return Response.success();
    }

}
