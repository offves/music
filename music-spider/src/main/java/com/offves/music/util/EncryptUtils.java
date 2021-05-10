package com.offves.music.util;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.Base64Utils;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

@Slf4j
public class EncryptUtils {

    /**
     * 密钥
     */
    private static final String NONCE = "0CoJUm6Qyw8W8jud";

    /**
     * 偏移量
     */
    private static final String IV_PARAMETER = "0102030405060708";

    /**
     * 公共密钥
     */
    private static final String PUB_KEY = "010001";

    /**
     * 基本指数
     */
    private static final String MODULUS = "00e0b509f6259df8642dbc35662901477df22677ec152b5ff68ace615bb7b725152b3ab17a876aea8a5aa76d2e417629ec4ee341f56135fccf695280104e0312ecbda92557c93870114af6c9d05c4f7f0c3685b7a46bee255932575cce10b424d813cfe4875d3e82047b97ddef52741d546b8e289dc6935b3ece0462db0a22b8e7";

    /**
     * 随机字符串
     * @param length 长度：取16
     */
    public static String createSecretKey(int length) {
        String base = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        Random random = new Random();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            int number = random.nextInt(base.length());
            sb.append(base.charAt(number));
        }
        return sb.toString();
    }

    /**
     * 字符串转换为16进制字符串
     * @param s 字符串
     */
    public static String stringToHexString(String s) {
        StringBuilder str = new StringBuilder();
        for (int i = 0; i < s.length(); i++) {
            int ch = s.charAt(i);
            String s4 = Integer.toHexString(ch);
            str.append(s4);
        }
        return str.toString();
    }

    /**
     * RSA 加密采用非常规填充方式，既不是PKCS1也不是PKCS1_OAEP，网易的做法是直接向前补0
     */
    public static String zfill(String str, int size) {
        StringBuilder strBuilder = new StringBuilder(str);
        while (strBuilder.length() < size)
            strBuilder.insert(0, "0");
        str = strBuilder.toString();
        return str;
    }

    /**
     * AES加密
     * 此处使用AES-128-CBC加密模式，key需要为16位
     * @param content 加密内容
     * @param sKey    偏移量
     */
    public static String aesCBCEncrypt(String content, String sKey) {
        try {
            byte[] byteContent = content.getBytes(StandardCharsets.UTF_8);
            // 获取cipher对象，getInstance("算法/工作模式/填充模式")
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            // 采用AES方式将密码转化成密钥
            SecretKeySpec secretKeySpec = new SecretKeySpec(sKey.getBytes(StandardCharsets.UTF_8), "AES");
            // 初始化偏移量
            IvParameterSpec iv = new IvParameterSpec(IV_PARAMETER.getBytes());
            // cipher对象初始化 init（“加密/解密,密钥，偏移量”）
            cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec, iv);
            // 数据处理
            byte[] encryptedBytes = cipher.doFinal(byteContent);
            // 此处使用BASE64做转码功能，同时能起到2次加密的作用
            return new String(Base64Utils.encode(encryptedBytes), StandardCharsets.UTF_8);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return StringUtils.EMPTY;
    }

    /**
     * RSA 加密
     * @param secKey 随机16位字符串
     */
    public static String rsaEncrypt(String secKey) {
        // 需要先将加密的消息翻转，再进行加密
        secKey = new StringBuffer(secKey).reverse().toString();
        // 转十六进制字符串
        String secKeyHex = stringToHexString(secKey);
        // 指定基数的数值字符串转换为BigInteger表示形式
        BigInteger biText = new BigInteger(secKeyHex, 16);
        BigInteger biEx = new BigInteger(PUB_KEY, 16);
        BigInteger biMod = new BigInteger(MODULUS, 16);
        // 次方并求余（biText^biEx mod biMod is ?）
        BigInteger bigInteger = biText.modPow(biEx, biMod);
        return zfill(bigInteger.toString(16), 256);
    }

    /**
     * 获取加密参数
     * @param data 加密内容
     */
    public static Map<String, String> encrypt(Map<String, Object> data) {
        String secKey = createSecretKey(16);
        // 二次AES加密、加密模式都是CBC加密
        // 第一次加密使用content和nonce进行加密
        // 第二次使用第一次加密结果和16位随机字符串进行加密
        String params = aesCBCEncrypt((aesCBCEncrypt(JSON.toJSONString(data), NONCE)), secKey);
        // RSA 加密
        String encSecKey = rsaEncrypt(secKey);

        Map<String, String> map = new HashMap<>();
        map.put("params", params);
        map.put("encSecKey", encSecKey);
        return map;
    }

}
