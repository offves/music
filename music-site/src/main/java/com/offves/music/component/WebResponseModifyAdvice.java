package com.offves.music.component;

import com.alipay.common.tracer.core.context.trace.SofaTraceContext;
import com.alipay.common.tracer.core.holder.SofaTraceContextHolder;
import com.offves.music.common.dto.Response;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import java.util.Objects;

@RestControllerAdvice
public class WebResponseModifyAdvice implements ResponseBodyAdvice<Response<?>> {

    @Override
    public boolean supports(final MethodParameter methodParameter, @NonNull final Class<? extends HttpMessageConverter<?>> converterType) {
        return Objects.requireNonNull(methodParameter.getMethod()).getReturnType().isAssignableFrom(Response.class) && converterType.isAssignableFrom(MappingJackson2HttpMessageConverter.class);
    }

    @Override
    public Response<?> beforeBodyWrite(Response<?> body, @NonNull MethodParameter returnType, @NonNull MediaType selectedContentType, @NonNull Class<? extends HttpMessageConverter<?>> selectedConverterType, @NonNull ServerHttpRequest request, @NonNull ServerHttpResponse response) {
        SofaTraceContext sofaTraceContext = SofaTraceContextHolder.getSofaTraceContext();
        if (Objects.nonNull(body) && Objects.nonNull(sofaTraceContext) && Objects.nonNull(sofaTraceContext.getCurrentSpan()) && Objects.nonNull(sofaTraceContext.getCurrentSpan().getSofaTracerSpanContext())) {
            body.setTraceId(sofaTraceContext.getCurrentSpan().getSofaTracerSpanContext().getTraceId());
        }
        return body;
    }

}
