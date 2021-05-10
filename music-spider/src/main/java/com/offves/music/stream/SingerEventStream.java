package com.offves.music.stream;

import com.offves.music.contract.api.SingerApi;
import com.offves.music.spider.SongSpider;
import com.offves.music.stream.event.SingerEvent;
import com.offves.music.stream.event.SongEvent;
import com.offves.music.stream.messaging.SingerProcessor;
import com.offves.stream.EventStream;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.messaging.Message;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.util.StopWatch;

import javax.annotation.Resource;
import java.util.List;
import java.util.Objects;

@Slf4j
@EnableBinding(SingerProcessor.class)
public class SingerEventStream implements EventStream<SingerEvent> {

    @Resource
    private SingerProcessor singerProcessor;

    @Resource
    private SongSpider songSpider;

    @Resource
    private SongEventStream songEventStream;

    @DubboReference
    private SingerApi singerApi;

    @Override
    public boolean out(SingerEvent event) {
        if (Objects.isNull(event.getSingerId())) {
            return false;
        }
        Message<SingerEvent> message = buildMessage(event);
        boolean result = singerProcessor.output().send(message);
        log.info("singer-topic send. singerId: {}, result: {}", event.getSingerId(), result);
        return result;
    }

    @Override
    @StreamListener(SingerProcessor.INPUT)
    public void sink(@Payload SingerEvent event) {
        try {
            Long singerId = event.getSingerId();

            StopWatch stopWatch = new StopWatch();
            stopWatch.start("singerSongs");
            List<Long> songIds = songSpider.singerSongs(singerId);
            stopWatch.stop();

            stopWatch.start("songEventStream.out");
            songEventStream.out(new SongEvent(songIds));
            stopWatch.stop();

            stopWatch.start("updateSingerSpiderStatus");
            singerApi.updateSingerSpiderStatus(singerId);
            stopWatch.stop();

            log.info("singer-topic sink. singerId: {}, totalTime: {} s, {}", singerId, stopWatch.getTotalTimeSeconds(), stopWatch.prettyPrint());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }

}
