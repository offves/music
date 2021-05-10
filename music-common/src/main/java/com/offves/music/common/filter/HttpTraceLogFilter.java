package com.offves.music.common.filter;

import com.offves.music.common.util.JsonUtil;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.Ordered;
import org.springframework.lang.NonNull;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;
import org.springframework.web.util.WebUtils;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

@Slf4j
public class HttpTraceLogFilter extends OncePerRequestFilter implements Ordered {

    private static final String IGNORE_CONTENT_TYPE = "multipart/form-data";

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull FilterChain filterChain) throws ServletException, IOException {
        if (!(request instanceof ContentCachingRequestWrapper)) {
            request = new ContentCachingRequestWrapper(request);
        }
        if (!(response instanceof ContentCachingResponseWrapper)) {
            response = new ContentCachingResponseWrapper(response);
        }

        long startTime = System.currentTimeMillis();

        try {
            filterChain.doFilter(request, response);
        } finally {
            String uri = request.getRequestURI();
            uri = URLDecoder.decode(uri, StandardCharsets.UTF_8.name());
            if (!Objects.equals(IGNORE_CONTENT_TYPE, request.getContentType())) {
                HttpTraceLog traceLog = HttpTraceLog.builder()
                        .uri(uri)
                        .method(request.getMethod())
                        .timeConsuming(System.currentTimeMillis() - startTime)
                        .parameterMap(JsonUtil.toString(request.getParameterMap()))
                        .requestBody(getRequestBody(request))
                        .responseBody(getResponseBody(response))
                        .build();
                log.info("Http Trace Log. uri: {}, method: {}, timeConsuming: {}, \r\nparameterMap: {}, \r\nrequestBody: {}, \r\nresponseBody: {}",
                        traceLog.getUri(), traceLog.getMethod(), traceLog.getTimeConsuming(), traceLog.getParameterMap(), traceLog.getRequestBody(), traceLog.getResponseBody());
            }
            updateResponse(response);
        }
    }

    private String getRequestBody(HttpServletRequest request) {
        String requestBody = StringUtils.EMPTY;
        ContentCachingRequestWrapper wrapper = WebUtils.getNativeRequest(request, ContentCachingRequestWrapper.class);
        if (wrapper != null) {
            requestBody = new String(wrapper.getContentAsByteArray(), StandardCharsets.UTF_8);
        }
        return requestBody;
    }

    private String getResponseBody(HttpServletResponse response) {
        String responseBody = StringUtils.EMPTY;
        ContentCachingResponseWrapper wrapper = WebUtils.getNativeResponse(response, ContentCachingResponseWrapper.class);
        if (wrapper != null) {
            responseBody = new String(wrapper.getContentAsByteArray(), StandardCharsets.UTF_8);
        }
        return responseBody;
    }

    private void updateResponse(HttpServletResponse response) throws IOException {
        ContentCachingResponseWrapper responseWrapper = WebUtils.getNativeResponse(response, ContentCachingResponseWrapper.class);
        Objects.requireNonNull(responseWrapper).copyBodyToResponse();
    }

    @Override
    public int getOrder() {
        return Ordered.LOWEST_PRECEDENCE - 10;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class HttpTraceLog {

        private String uri;

        private String method;

        private Long timeConsuming;

        private String parameterMap;

        private Object requestBody;

        private Object responseBody;

    }

}