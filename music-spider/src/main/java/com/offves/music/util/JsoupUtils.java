package com.offves.music.util;

import lombok.extern.slf4j.Slf4j;
import org.jsoup.Connection;
import org.jsoup.Jsoup;

import java.io.IOException;
import java.util.Map;

@Slf4j
public class JsoupUtils {

    public static String post(String url, Map<String, String> data) throws IOException {
        return post(url, data, null);
    }

    public static String post(String url, Map<String, String> data, Map<String, String> headers) throws IOException {
        return Jsoup.connect(url)
                // .proxy("127.0.0.1", 7890)
                .data(data)
                .headers(headers)
                .method(Connection.Method.POST)
                .ignoreContentType(true)
                .timeout(100000)
                .execute()
                .body();
    }

}
