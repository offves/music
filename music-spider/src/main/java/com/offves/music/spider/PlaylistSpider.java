package com.offves.music.spider;

import com.offves.music.api.Api;
import com.offves.music.dto.PersonalizedDto;
import com.offves.music.dto.PlaylistDetailDto;
import com.offves.music.stream.PlaylistEventStream;
import com.offves.music.stream.event.PlaylistEvent;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class PlaylistSpider implements CommandLineRunner {

    @Resource
    private PlaylistEventStream playlistEventStream;

    @Override
    public void run(String... args) {
        // personalized();
    }

    public void personalized(Integer limit) {
        List<PersonalizedDto> personalized = Api.personalized(limit);
        for (PersonalizedDto playlist : personalized) {
            playlistEventStream.out(new PlaylistEvent(playlist.getId()));
        }
    }

    public PlaylistDetailDto playlistDetail(Long playlistId) {
        return Api.playlistDetail(playlistId);
    }

}
