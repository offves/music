package com.offves.music.api;

import java.util.HashMap;
import java.util.Map;

public class Params {

    private final Map<String, Object> map = new HashMap<>();

    public static Params builder() {
        return new Params();
    }

    public Params put(String key, Object value) {
        map.put(key, value);
        return this;
    }

    public Map<String, Object> build() {
        return this.map;
    }

}
