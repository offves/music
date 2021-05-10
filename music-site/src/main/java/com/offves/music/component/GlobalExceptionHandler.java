package com.offves.music.component;

import com.offves.music.common.dto.Response;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.http.HttpStatus;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ResponseStatus
    @ExceptionHandler(NoHandlerFoundException.class)
    public Response<?> handlerNoFoundException(NoHandlerFoundException e) {
        prepareForException(e);
        return Response.error(HttpStatus.NOT_FOUND.value(), "404 Not Found.");
    }

    @ResponseStatus
    @ExceptionHandler(Exception.class)
    public Response<?> handleException(Exception e) {
        prepareForException(e);
        return Response.error(e.getMessage());
    }

    @ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public Response<?> httpRequestMethodNotSupportedException(Exception e) {
        prepareForException(e);
        return Response.error("not support request method.");
    }

    private void prepareForException(Throwable throwable) {
        Throwable rootCause = ExceptionUtils.getRootCause(throwable);
        log.error(rootCause.getMessage(), rootCause);
    }

}
