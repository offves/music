package com.offves.music.util;

import com.alibaba.fastjson.JSON;
import jdk.nashorn.api.scripting.ScriptObjectMirror;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.ResourceUtils;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

@Slf4j
public class JSSecretUtils {

    private static Invocable inv;

    private static final String encText = "encText";

    private static final String encSecKey = "encSecKey";

    /*
     * 从本地加载修改后的 js 文件到 scriptEngine
     */
    static {
        try {
            String pathResources = ResourceUtils.getURL("classpath:").getPath();
            pathResources = pathResources + "neteaseMusicCore.js";
            Path path = Paths.get(pathResources);
            byte[] bytes = Files.readAllBytes(path);
            String js = new String(bytes);
            ScriptEngineManager factory = new ScriptEngineManager();
            ScriptEngine engine = factory.getEngineByName("JavaScript");
            engine.eval(js);
            inv = (Invocable) engine;
            System.out.println("Init completed");
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }

    public static Map<String, String> encrypt(Map<String, Object> data) {
        try {
            ScriptObjectMirror so = (ScriptObjectMirror) inv.invokeFunction("encrypt", JSON.toJSONString(data));
            HashMap<String, String> map = new HashMap<>();
            map.put("params", so.get(JSSecretUtils.encText).toString());
            map.put("encSecKey", so.get(JSSecretUtils.encSecKey).toString());
            return map;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return null;
        }
    }

    public static Map<String, String> getData(String paras) {
        try {
            ScriptObjectMirror so = (ScriptObjectMirror) inv.invokeFunction("encrypt", paras);
            HashMap<String, String> map = new HashMap<>();
            map.put("params", so.get(JSSecretUtils.encText).toString());
            map.put("encSecKey", so.get(JSSecretUtils.encSecKey).toString());
            return map;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return null;
        }
    }

}