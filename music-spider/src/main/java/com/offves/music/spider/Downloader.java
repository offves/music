package com.offves.music.spider;

import com.mpatric.mp3agic.ID3v2;
import com.mpatric.mp3agic.Mp3File;
import com.offves.music.api.Api;
import com.offves.music.dto.PlaylistDetailDto;
import com.offves.music.dto.TrackDto;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
public class Downloader {

    public static void download(String folder, Long playlistId) {
        PlaylistDetailDto playlistDetail = Api.playlistDetail(playlistId);

        List<TrackDto> tracks = playlistDetail.getTracks();
        List<Long> songIds = tracks.stream().map(TrackDto::getId).collect(Collectors.toList());

        List<Pair<Long, String>> pairs = Api.songUrl(songIds);
        Map<Long, String> dict = pairs.stream().collect(Collectors.toMap(Pair::getLeft, Pair::getRight));

        for (TrackDto track : tracks) {
            Long musicId = track.getId();
            String name = track.getName();
            String downloadUrl = dict.get(musicId);
            if (StringUtils.isBlank(downloadUrl)) {
                continue;
            }

            String suffix = downloadUrl.substring(downloadUrl.lastIndexOf("."));
            String filePath = folder + "/" + playlistId + "/" + name + suffix;

            byte[] bytes = getUrlBytes(downloadUrl);
            if (bytes == null || bytes.length <= 0)
                return;

            try {
                File file = new File(filePath + ".bak");
                FileUtils.writeByteArrayToFile(file, bytes);

                Mp3File mp3File = new Mp3File(filePath + ".bak");
                ID3v2 id3v2Tag = mp3File.getId3v2Tag();
                if (track.getAl() != null && StringUtils.isNotBlank(track.getAl().getPicUrl())) {
                    id3v2Tag.setAlbumImage(getUrlBytes(track.getAl().getPicUrl()), "image/jpeg");
                }

                // 歌词
                String lyric = Api.lyric(musicId);
                id3v2Tag.setLyrics(lyric);

                mp3File.save(filePath);

                FileUtils.forceDelete(file);

                log.info("download {}", name);
            } catch (Exception e) {
                log.error(e.getMessage(), e);
            }
        }
    }

    private static byte[] getUrlBytes(String uri) {
        InputStream input = null;
        ByteArrayOutputStream output = null;
        try {
            URL url = new URL(uri);
            URLConnection conn = url.openConnection();
            input = conn.getInputStream();

            output = new ByteArrayOutputStream();
            byte[] buffer = new byte[4096];
            int n;
            while (-1 != (n = input.read(buffer))) {
                output.write(buffer, 0, n);
            }
            return output.toByteArray();
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        } finally {
            if (input != null) {
                try {
                    input.close();
                } catch (IOException e) {
                    log.error(e.getMessage(), e);
                }
            }
            if (output != null) {
                try {
                    output.close();
                } catch (IOException e) {
                    log.error(e.getMessage(), e);
                }
            }
        }
        return null;
    }

}
