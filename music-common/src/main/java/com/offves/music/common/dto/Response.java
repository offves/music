package com.offves.music.common.dto;

import com.offves.music.common.enums.ResponseEnum;
import lombok.Data;

import java.io.Serializable;

@Data
public class Response<T> implements Serializable {

    private static final long serialVersionUID = 793247293007112531L;

    private boolean success = true;

    private Integer code;

    private String message;

    private String traceId;

    private T result;

    private long timestamp = System.currentTimeMillis();

    public Response() {
    }

    public static <T> Response<T> success() {
        Response<T> r = new Response<>();
        r.setSuccess(true);
        r.setCode(ResponseEnum.SUCCESS.getCode());
        r.setMessage(ResponseEnum.SUCCESS.getMessage());
        return r;
    }

    public static <T> Response<T> success(T data) {
        Response<T> r = new Response<>();
        r.setSuccess(true);
        r.setCode(ResponseEnum.SUCCESS.getCode());
        r.setMessage(ResponseEnum.SUCCESS.getMessage());
        r.setResult(data);
        return r;
    }

    public Response<T> success(String msg) {
        Response<T> r = new Response<>();
        r.setSuccess(true);
        r.setCode(ResponseEnum.SUCCESS.getCode());
        r.setMessage(msg);
        return r;
    }

    public static <T> Response<T> error(ResponseEnum responseEnum) {
        return error(responseEnum.getCode(), responseEnum.getMessage());
    }

    public static <T> Response<T> error() {
        return error(ResponseEnum.SERVER_ERROR.getCode(), ResponseEnum.SERVER_ERROR.getMessage());
    }

    public static <T> Response<T> error(String msg) {
        return error(ResponseEnum.SERVER_ERROR.getCode(), msg);
    }

    public static <T> Response<T> error(int code, String msg) {
        Response<T> r = new Response<>();
        r.setCode(code);
        r.setMessage(msg);
        r.setSuccess(false);
        return r;
    }

    public boolean isFail() {
        return !this.isSuccess();
    }

}
