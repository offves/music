package com.offves.music.api;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Maps;
import com.google.common.net.HttpHeaders;
import com.offves.music.common.Constant;
import com.offves.music.dto.*;
import com.offves.music.util.EncryptUtils;
import com.offves.music.util.JsoupUtils;
import com.offves.music.util.UserAgentUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
public class Api {

    private static final String BASE_URL = "https://music.163.com";

    /**
     * 推荐歌单
     * @param limit max 100
     */
    public static List<PersonalizedDto> personalized(Integer limit) {
        if (limit == null || limit <= 0) {
            return Collections.emptyList();
        }

        Map<String, Object> params = Params.builder().put("limit", limit).put("total", true).put("n", 1000).build();
        String res = post(BASE_URL + "/weapi/personalized/playlist", params);
        JSONObject obj = JSON.parseObject(res);
        if (Objects.equals(obj.getInteger(Constant.CODE_KEY), Constant.SUCCESS_CODE)) {
            return obj.getJSONArray(Constant.NODEAPI_RESULT_KEY).toJavaList(PersonalizedDto.class);
        }
        return Collections.emptyList();
    }

    /**
     * 歌单详情-含歌曲列表 id
     */
    public static PlaylistDetailDto playlistDetail(Long playlistId) {
        Map<String, Object> params = Params.builder().put("id", playlistId).put("n", 100000).build();
        String res = post(BASE_URL + "/weapi/v3/playlist/detail", params);
        JSONObject obj = JSON.parseObject(res);
        if (Objects.equals(obj.getInteger(Constant.CODE_KEY), Constant.SUCCESS_CODE)) {
            return obj.getObject("playlist", PlaylistDetailDto.class);
        }
        return null;
    }

    /**
     * 歌曲详情
     */
    public static List<SongDetailDto> songDetail(List<Long> songIds) {
        if (CollectionUtils.isEmpty(songIds)) {
            return Collections.emptyList();
        }

        List<Map<String, Long>> c = new ArrayList<>();
        songIds.forEach(p -> {
            Map<String, Long> map = new HashMap<>();
            map.put("id", p);
            c.add(map);
        });

        String ids = songIds.stream().map(String::valueOf).collect(Collectors.joining(","));
        Map<String, Object> params = Params.builder().put("ids", ids).put("c", JSON.toJSONString(c)).build();
        String res = post(BASE_URL + "/weapi/v3/song/detail", params);
        try {
            JSONObject obj = JSON.parseObject(res);
            if (Objects.equals(obj.getInteger(Constant.CODE_KEY), Constant.SUCCESS_CODE)) {
                return obj.getJSONArray("songs").toJavaList(SongDetailDto.class);
            }
        } catch (Exception e) {
            log.error(e.getMessage(), res);
            throw e;
        }
        return Collections.emptyList();
    }

    /**
     * 歌词
     */
    public static String lyric(Long songId) {
        if (songId == null || songId <= 0) {
            return StringUtils.EMPTY;
        }
        Map<String, Object> params = Params.builder().put("id", songId).put("lv", -1).put("kv", -1).put("tv", -1).build();
        String res = post(BASE_URL + "/weapi/song/lyric", params);
        try {
            JSONObject obj = JSON.parseObject(res);
            if (Objects.equals(obj.getInteger(Constant.CODE_KEY), Constant.SUCCESS_CODE)) {
                JSONObject lrc = obj.getJSONObject("lrc");
                return lrc == null ? StringUtils.EMPTY : lrc.getString("lyric");
            }
        } catch (Exception e) {
            log.error(e.getMessage(), res);
            throw e;
        }
        return StringUtils.EMPTY;
    }

    /**
     * 歌曲 url
     */
    public static List<Pair<Long, String>> songUrl(List<Long> songIds) {
        if (CollectionUtils.isEmpty(songIds)) {
            return Collections.emptyList();
        }
        String ids = songIds.stream().map(String::valueOf).collect(Collectors.joining(","));
        Map<String, Object> params = Params.builder().put("ids", "[" + ids + "]").put("br", 999000).build();
        String res = post(BASE_URL + "/weapi/song/enhance/player/url", params);

        JSONObject obj = JSON.parseObject(res);
        if (Objects.equals(obj.getInteger(Constant.CODE_KEY), Constant.SUCCESS_CODE)) {
            @SuppressWarnings("rawtypes")
            List<Map> data = obj.getJSONArray("data").toJavaList(Map.class);
            if (!CollectionUtils.isEmpty(data)) {
                List<Pair<Long, String>> songUrls = new ArrayList<>();
                data.forEach(o -> {
                    Long id = Long.valueOf((Integer) o.get("id"));
                    String url = (String) o.get("url");
                    if (StringUtils.isNotBlank(url)) {
                        songUrls.add(Pair.of(id, url));
                    }
                });
                return songUrls;
            }
        }
        return Collections.emptyList();
    }

    /**
     * 歌曲评论
     * @param songId 歌曲 id
     * @param offset 页码
     * @param before 上一页最后的 time, 超过 5000 条评论的时候需要用到
     */
    public static Pair<Integer, List<CommentDto>> songComment(Long songId, Integer offset, Long before) {
        Map<String, Object> params = Params.builder()
                .put("rid", songId)
                .put("limit", 20)
                .put("offset", offset == null ? 0 : offset)
                .put("before", before == null ? 0 : before)
                .build();
        String res = post(BASE_URL + "/weapi/v1/resource/comments/R_SO_4_" + songId, params);
        JSONObject obj = JSON.parseObject(res);
        if (Objects.equals(obj.getInteger(Constant.CODE_KEY), Constant.SUCCESS_CODE)) {
            List<CommentDto> comments = obj.getJSONArray("comments").toJavaList(CommentDto.class);
            Integer total = obj.getInteger("total");
            return Pair.of(total, comments);
        }
        return Pair.of(0, Collections.emptyList());
    }

    /**
     * 用户信息
     */
    public static UserDto userProfile(Long userId) {
        if (userId == null || userId <= 0) {
            return new UserDto();
        }

        String res = post(BASE_URL + "/weapi/v1/user/detail/" + userId, Maps.newHashMap());
        JSONObject obj = JSON.parseObject(res);
        if (Objects.equals(obj.getInteger(Constant.CODE_KEY), Constant.SUCCESS_CODE)) {
            UserDto user = obj.getObject("profile", UserDto.class);
            user.setLevel(obj.getInteger("level"));
            user.setListenSongs(obj.getLong("listenSongs"));
            return user;
        }

        return null;
    }

    public static List<SongDetailDto> getSingerSongs2(Long singerId, Integer limit, Integer offset) {
        Pair<Boolean, List<SongDetailDto>> pair = getSingerSongs(singerId, limit, offset);
        return pair.getRight();
    }

    public static Pair<Boolean, List<SongDetailDto>> getSingerSongs(Long singerId, Integer limit, Integer offset) {
        Map<String, Object> params = Params.builder()
                .put("id", singerId)
                .put("private_cloud", "true")
                .put("work_type", 1)
                .put("order", "hot")// hot,time
                .put("limit", limit)
                .put("offset", offset)
                .build();
        String res = post(BASE_URL + "/weapi/v1/artist/songs", params);
        JSONObject obj = JSON.parseObject(res);

        Assert.notNull(obj, "get singer songs response body is null");

        if (Objects.equals(obj.getInteger(Constant.CODE_KEY), Constant.SUCCESS_CODE)) {
            return Pair.of(obj.getBoolean("more"), obj.getJSONArray("songs").toJavaList(SongDetailDto.class));
        }

        return Pair.of(false, Collections.emptyList());
    }

    public static Integer getSingerSongsTotal(Long singerId, Integer limit, Integer offset) {
        Map<String, Object> params = Params.builder()
                .put("id", singerId)
                .put("private_cloud", "true")
                .put("work_type", 1)
                .put("order", "hot")// hot,time
                .put("limit", limit)
                .put("offset", offset)
                .build();
        String res = post(BASE_URL + "/weapi/v1/artist/songs", params);
        JSONObject obj = JSON.parseObject(res);

        Assert.notNull(obj, "get singer songs response body is null");

        if (Objects.equals(obj.getInteger(Constant.CODE_KEY), Constant.SUCCESS_CODE)) {
            return obj.getInteger("total");
        }

        return 0;
    }

    private static String post(String url, Map<String, Object> data) {
        Map<String, String> headers = new HashMap<>();
        headers.put(HttpHeaders.HOST, "music.163.com");
        headers.put(HttpHeaders.CONTENT_TYPE, "application/x-www-form-urlencoded");
        headers.put(HttpHeaders.USER_AGENT, UserAgentUtils.userAgent());

        try {
            // log.info("request {}, param {}", url, JSON.toJSONString(data));
            return JsoupUtils.post(url, EncryptUtils.encrypt(data), headers);
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }
        return StringUtils.EMPTY;
    }

}
